package com.anthill.app.data.dao

import androidx.room.*
import com.anthill.app.data.model.Category
import com.anthill.app.data.model.Item

@Dao
interface ItemDao {
    @Insert
    suspend fun insertCategory(category: Category): Long

    @Insert
    suspend fun insertItem(item: Item): Long

    @Query("SELECT * FROM items WHERE sku = :sku AND is_active = 1")
    suspend fun getItemBySku(sku: String): Item?

    @Query("SELECT * FROM categories WHERE is_active = 1")
    suspend fun getAllCategories(): List<Category>
}
