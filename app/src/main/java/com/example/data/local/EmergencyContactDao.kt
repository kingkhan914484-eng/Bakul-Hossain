package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {
    @Query("SELECT * FROM emergency_contacts WHERE verified = 'Yes' AND status = 'Active' ORDER BY category ASC, name ASC")
    fun getAllVerifiedContacts(): Flow<List<EmergencyContactEntity>>

    @Query("SELECT * FROM emergency_contacts ORDER BY category ASC, name ASC")
    fun getAllContactsAdmin(): Flow<List<EmergencyContactEntity>>

    @Query("SELECT * FROM emergency_contacts WHERE verified = 'Yes' AND status = 'Active' ORDER BY category ASC, name ASC")
    suspend fun getAllVerifiedContactsSync(): List<EmergencyContactEntity>

    @Query("SELECT COUNT(*) FROM emergency_contacts")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM emergency_contacts WHERE verified = 'Yes' AND status = 'Active'")
    suspend fun getVerifiedActiveCount(): Int

    @Query("SELECT COUNT(*) FROM emergency_contacts WHERE verified = 'No' OR status != 'Active'")
    suspend fun getNeedsVerificationCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<EmergencyContactEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: EmergencyContactEntity)

    @Query("UPDATE emergency_contacts SET status = :status, verified = :verified, verifiedAt = :verifiedAt, reviewDue = :reviewDue WHERE id = :id")
    suspend fun updateStatus(id: String, status: String, verified: String, verifiedAt: String, reviewDue: String)

    @Query("DELETE FROM emergency_contacts WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM emergency_contacts")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceVerifiedContacts(contacts: List<EmergencyContactEntity>) {
        clearAll()
        insertAll(contacts)
    }
}
