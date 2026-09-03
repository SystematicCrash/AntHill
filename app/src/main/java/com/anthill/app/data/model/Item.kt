package com.anthill.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "is_active") override val isActive: Boolean = true,
    @ColumnInfo(name = "created_by") override val createdBy: String,
    @ColumnInfo(name = "updated_by") override val updatedBy: String,
    @ColumnInfo(name = "created_at") override val createdAt: Long,
    @ColumnInfo(name = "updated_at") override val updatedAt: Long
) : AuditEntity, SoftDeleteEntity

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"]
        )
    ]
)
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "sku") val sku: String,
    val barcode: String,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "unit_of_measure") val unitOfMeasure: String,
    @ColumnInfo(name = "min_stock_level") val minStockLevel: Int,
    @ColumnInfo(name = "max_stock_level") val maxStockLevel: Int,
    @ColumnInfo(name = "is_active") override val isActive: Boolean = true,
    @ColumnInfo(name = "created_by") override val createdBy: String,
    @ColumnInfo(name = "updated_by") override val updatedBy: String,
    @ColumnInfo(name = "created_at") override val createdAt: Long,
    @ColumnInfo(name = "updated_at") override val updatedAt: Long
) : AuditEntity, SoftDeleteEntity
