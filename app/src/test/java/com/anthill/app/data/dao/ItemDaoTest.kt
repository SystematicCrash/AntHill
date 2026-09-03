package com.anthill.app.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.anthill.app.data.AppDatabase
import com.anthill.app.data.model.Category
import com.anthill.app.data.model.Item
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ItemDaoTest {
    private lateinit var itemDao: ItemDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        itemDao = db.itemDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `should insert and retrieve item with category`() = runBlocking {
        val category = Category(name = "Electronics", createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0)
        val catId = itemDao.insertCategory(category)
        
        val item = Item(
            name = "Laptop", sku = "LP-01", barcode = "123", categoryId = catId,
            unitOfMeasure = "piece", minStockLevel = 1, maxStockLevel = 10,
            createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0
        )
        itemDao.insertItem(item)
        
        val retrieved = itemDao.getItemBySku("LP-01")
        assertEquals("Laptop", retrieved?.name)
        assertEquals(catId, retrieved?.categoryId)
    }
}
