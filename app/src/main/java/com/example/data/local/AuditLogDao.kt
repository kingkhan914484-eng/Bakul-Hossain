package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditLogDao {
    @Query("SELECT * FROM audit_logs ORDER BY changedAt DESC LIMIT 100")
    fun getAllLogs(): Flow<List<AuditLogEntity>>

    @Insert
    suspend fun insert(log: AuditLogEntity)
}
