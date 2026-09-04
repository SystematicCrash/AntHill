package com.anthill.app.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.anthill.app.data.AppDatabase
import com.anthill.app.data.model.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserDaoTest {
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `should insert and retrieve active user`() = runBlocking {
        val user = User(username="sadra", passwordHash="pass", fullName="Sadra", createdBy="admin", updatedBy="admin", createdAt=0, updatedAt=0)
        val id = userDao.insertUser(user)
        val retrieved = userDao.getUserById(id)
        assertNotNull(retrieved)
        assertEquals("sadra", retrieved?.username)
    }

    @Test
    fun `should not retrieve deactivated user`() = runBlocking {
        val user = User(username="sadra", passwordHash="pass", fullName="Sadra", createdBy="admin", updatedBy="admin", createdAt=0, updatedAt=0)
        val id = userDao.insertUser(user)
        userDao.deactivateUser(id)
        val retrieved = userDao.getUserById(id)
        assertNull(retrieved)
    }
    
    @Test
    fun `should update user`() = runBlocking {
        val user = User(username="sadra", passwordHash="pass", fullName="Sadra", createdBy="admin", updatedBy="admin", createdAt=0, updatedAt=0)
        val id = userDao.insertUser(user)
        val updatedUser = user.copy(id = id, fullName = "Sadra Izady")
        userDao.updateUser(updatedUser)
        val retrieved = userDao.getUserById(id)
        assertEquals("Sadra Izady", retrieved?.fullName)
    }

    @Test
    fun `should return null when searching for non-existent user id`() = runBlocking {
        val retrieved = userDao.getUserById(999L)
        assertNull(retrieved)
    }

    @Test
    fun `should return null when searching for non-existent username`() = runBlocking {
        val retrieved = userDao.getUserByUsername("unknown")
        assertNull(retrieved)
    }
}
