package com.anthill.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "roles")
data class Role(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name = "is_active") override val isActive: Boolean = true,
    @ColumnInfo(name = "created_by") override val createdBy: String = "system",
    @ColumnInfo(name = "updated_by") override val updatedBy: String = "system",
    @ColumnInfo(name = "created_at") override val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") override val updatedAt: Long = System.currentTimeMillis()
) : AuditEntity, SoftDeleteEntity

@Entity(tableName = "permissions")
data class Permission(
    @PrimaryKey val id: Long,
    val name: String, // e.g., "RECEIPT_CREATE"
    val description: String
)

@Entity(tableName = "user_roles", primaryKeys = ["userId", "roleId"])
data class UserRoleCrossRef(
    val userId: Long,
    val roleId: Long
)
