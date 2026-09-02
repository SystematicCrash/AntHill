package com.anthill.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val passwordHash: String, // Store salted hash
    val fullName: String,
    @ColumnInfo(name = "is_active") override val isActive: Boolean = true,
    @ColumnInfo(name = "created_by") override val createdBy: String,
    @ColumnInfo(name = "updated_by") override val updatedBy: String,
    @ColumnInfo(name = "created_at") override val createdAt: Long,
    @ColumnInfo(name = "updated_at") override val updatedAt: Long
) : AuditEntity, SoftDeleteEntity
