package com.anthill.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "locations",
    foreignKeys = [
        ForeignKey(
            entity = Location::class,
            parentColumns = ["id"],
            childColumns = ["parent_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "location_code") val locationCode: String,
    val name: String,
    @ColumnInfo(name = "parent_id") val parentId: Long? = null,
    @ColumnInfo(name = "location_type") val locationType: String,
    val capacity: Double? = null,
    @ColumnInfo(name = "is_active") override val isActive: Boolean = true,
    @ColumnInfo(name = "created_by") override val createdBy: String,
    @ColumnInfo(name = "updated_by") override val updatedBy: String,
    @ColumnInfo(name = "created_at") override val createdAt: Long,
    @ColumnInfo(name = "updated_at") override val updatedAt: Long
) : AuditEntity, SoftDeleteEntity
