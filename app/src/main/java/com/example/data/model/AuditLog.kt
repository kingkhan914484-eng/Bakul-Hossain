package com.example.data.model

data class AuditLog(
    val id: Long = 0,
    val recordId: String,
    val action: String, // "VERIFIED", "STATUS_CHANGE", "UPDATED", "DEACTIVATED", "CREATED"
    val changedBy: String,
    val changedAt: Long,
    val previousValue: String,
    val newValue: String
)
