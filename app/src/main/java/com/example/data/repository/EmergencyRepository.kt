package com.example.data.repository

import android.content.Context
import com.example.data.local.EmergencyContactDao
import com.example.data.local.EmergencyContactEntity
import com.example.data.local.EmergencyDatabase
import com.example.data.model.EmergencyContact
import com.example.data.seed.EmergencyDirectorySeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class RefreshResult {
    data class Success(val count: Int, val isOffline: Boolean, val timestamp: String) : RefreshResult()
    data class Error(val message: String, val cachedCount: Int) : RefreshResult()
}

class EmergencyRepository(
    private val context: Context,
    private val database: EmergencyDatabase = EmergencyDatabase.getInstance(context),
    private val contactDao: EmergencyContactDao = database.contactDao(),
    private val auditLogDao: com.example.data.local.AuditLogDao = database.auditLogDao()
) {
    private val prefs = context.getSharedPreferences("bm_emergency_prefs", Context.MODE_PRIVATE)

    val verifiedContactsFlow: Flow<List<EmergencyContact>> =
        contactDao.getAllVerifiedContacts().map { entities ->
            val currentDate = getCurrentDateIso()
            // Strict filtering rule: Only publicly visible, non-expired, verified active records
            entities.map { it.toModel() }.filter { it.isPubliclyVisible(currentDate) }
        }

    val allAdminContactsFlow: Flow<List<EmergencyContact>> =
        contactDao.getAllContactsAdmin().map { entities ->
            entities.map { it.toModel() }
        }

    val auditLogsFlow: Flow<List<com.example.data.model.AuditLog>> =
        auditLogDao.getAllLogs().map { entities ->
            entities.map { it.toModel() }
        }

    suspend fun getVerifiedCount(): Int = withContext(Dispatchers.IO) {
        contactDao.getVerifiedActiveCount()
    }

    suspend fun getNeedsVerificationCount(): Int = withContext(Dispatchers.IO) {
        contactDao.getNeedsVerificationCount()
    }

    suspend fun updateContactStatus(
        id: String,
        newStatus: String,
        newVerified: String,
        adminUser: String = "Admin"
    ) = withContext(Dispatchers.IO) {
        val currentDate = getCurrentDateIso()
        val reviewDueDate = "2027-08-01"
        contactDao.updateStatus(id, newStatus, newVerified, currentDate, reviewDueDate)
        auditLogDao.insert(
            com.example.data.local.AuditLogEntity(
                recordId = id,
                action = "STATUS_UPDATE",
                changedBy = adminUser,
                changedAt = System.currentTimeMillis(),
                previousValue = "Unknown",
                newValue = "Status=$newStatus, Verified=$newVerified"
            )
        )
    }

    suspend fun addContact(
        contact: EmergencyContact,
        adminUser: String = "Admin"
    ) = withContext(Dispatchers.IO) {
        contactDao.insert(EmergencyContactEntity.fromModel(contact))
        auditLogDao.insert(
            com.example.data.local.AuditLogEntity(
                recordId = contact.id,
                action = "CREATE_RECORD",
                changedBy = adminUser,
                changedAt = System.currentTimeMillis(),
                previousValue = "None",
                newValue = "${contact.name} (${contact.category})"
            )
        )
    }

    suspend fun deleteContact(
        id: String,
        adminUser: String = "Admin"
    ) = withContext(Dispatchers.IO) {
        contactDao.deleteById(id)
        auditLogDao.insert(
            com.example.data.local.AuditLogEntity(
                recordId = id,
                action = "DELETE_RECORD",
                changedBy = adminUser,
                changedAt = System.currentTimeMillis(),
                previousValue = id,
                newValue = "Deleted"
            )
        )
    }

    fun getLastSyncTime(): String {
        return prefs.getString("last_sync_time", "") ?: ""
    }

    private fun setLastSyncTime(timeStr: String) {
        prefs.edit().putString("last_sync_time", timeStr).apply()
    }

    private fun getCurrentDateIso(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    private fun getCurrentTimestampHuman(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    suspend fun initializeIfEmpty() = withContext(Dispatchers.IO) {
        val count = contactDao.getCount()
        if (count == 0) {
            val currentDate = getCurrentDateIso()
            val validSeed = EmergencyDirectorySeed.initialVerifiedContacts.filter {
                it.isPubliclyVisible(currentDate)
            }
            val entities = validSeed.map { EmergencyContactEntity.fromModel(it) }
            contactDao.insertAll(entities)
            setLastSyncTime(getCurrentTimestampHuman())
        }
    }

    /**
     * Refresh database records:
     * - Validates every record according to strict publishing guidelines
     * - Filters out unverified, inactive, expired, non-https, or fake phone numbers
     * - Updates the Room cache
     */
    suspend fun refreshDirectory(isOnline: Boolean): RefreshResult = withContext(Dispatchers.IO) {
        try {
            val currentDate = getCurrentDateIso()
            val timestamp = getCurrentTimestampHuman()

            if (!isOnline) {
                // Offline fallback
                val cached = contactDao.getAllVerifiedContactsSync()
                    .map { it.toModel() }
                    .filter { it.isPubliclyVisible(currentDate) }
                val lastTime = getLastSyncTime().ifBlank { timestamp }
                return@withContext RefreshResult.Success(
                    count = cached.size,
                    isOffline = true,
                    timestamp = lastTime
                )
            }

            // In production environment with cloud backend, this would fetch from the Cloud API.
            // Here we validate the verified directory repository items.
            val rawIncomingList = EmergencyDirectorySeed.initialVerifiedContacts

            val strictlyValidated = rawIncomingList.filter { contact ->
                contact.isPubliclyVisible(currentDate)
            }

            val entities = strictlyValidated.map { EmergencyContactEntity.fromModel(it) }
            contactDao.replaceVerifiedContacts(entities)
            setLastSyncTime(timestamp)

            RefreshResult.Success(
                count = strictlyValidated.size,
                isOffline = false,
                timestamp = timestamp
            )
        } catch (e: Exception) {
            val cached = contactDao.getAllVerifiedContactsSync()
                .map { it.toModel() }
                .filter { it.isPubliclyVisible(getCurrentDateIso()) }
            RefreshResult.Error(
                message = e.localizedMessage ?: "Sync error occurred",
                cachedCount = cached.size
            )
        }
    }
}
