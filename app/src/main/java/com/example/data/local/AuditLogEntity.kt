package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AuditLog

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordId: String,
    val action: String,
    val changedBy: String,
    val changedAt: Long,
    val previousValue: String,
    val newValue: String
) {
    fun toModel(): AuditLog = AuditLog(
        id = id,
        recordId = recordId,
        action = action,
        changedBy = changedBy,
        changedAt = changedAt,
        previousValue = previousValue,
        newValue = newValue
    )

    companion object {
        fun fromModel(model: AuditLog): AuditLogEntity = AuditLogEntity(
            id = model.id,
            recordId = model.recordId,
            action = model.action,
            changedBy = model.changedBy,
            changedAt = model.changedAt,
            previousValue = model.previousValue,
            newValue = model.newValue
        )
    }
}
