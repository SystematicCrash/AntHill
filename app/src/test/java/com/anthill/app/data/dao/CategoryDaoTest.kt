package com.anthill.app.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.anthill.app.data.AppDatabase
import com.anthill.app.data.model.Category
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CategoryDaoTest {
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
    fun `should insert and retrieve categories`() = runBlocking {
        val cat1 = Category(name = "Electronics", createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0)
        val cat2 = Category(name = "Furniture", createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0)
        itemDao.insertCategory(cat1)
        itemDao.insertCategory(cat2)
        
        val categories = itemDao.getAllCategories()
        assertEquals(2, categories.size)
    }
}
