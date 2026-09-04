package com.anthill.app.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.anthill.app.data.AppDatabase
import com.anthill.app.data.model.Location
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocationDaoTest {
    private lateinit var locationDao: LocationDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        locationDao = db.locationDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `should insert and retrieve nested locations`() = runBlocking {
        val root = Location(locationCode = "WH", name = "Warehouse", locationType = "warehouse", createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0)
        val rootId = locationDao.insertLocation(root)
        
        val child = Location(locationCode = "HallA", name = "Hall A", parentId = rootId, locationType = "hall", createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0)
        locationDao.insertLocation(child)
        
        val children = locationDao.getChildren(rootId)
        assertEquals(1, children.size)
        assertEquals("Hall A", children[0].name)
    }
}
