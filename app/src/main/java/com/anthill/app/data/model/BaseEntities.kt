package com.anthill.app.data.model

import androidx.room.ColumnInfo
import java.time.Instant

/**
 * Audit pattern for tracking changes.
 */
interface AuditEntity {
    val createdBy: String
    val updatedBy: String
    val createdAt: Long
    val updatedAt: Long
}

interface SoftDeleteEntity {
    val isActive: Boolean
}
