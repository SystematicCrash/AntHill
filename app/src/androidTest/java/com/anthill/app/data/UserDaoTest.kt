package com.anthill.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.anthill.app.data.dao.UserDao
import com.anthill.app.data.model.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runBlocking {
        val user = User(
            username = "testuser",
            passwordHash = "hash",
            fullName = "Test User",
            createdBy = "admin",
            updatedBy = "admin",
            createdAt = 1234567890L,
            updatedAt = 1234567890L
        )
        val id = userDao.insertUser(user)
        val byId = userDao.getUserById(id)
        assertEquals(user.username, byId?.username)
    }

    @Test
    fun deactivateUser() = runBlocking {
        val user = User(
            username = "activeuser",
            passwordHash = "hash",
            fullName = "Active User",
            createdBy = "admin",
            updatedBy = "admin",
            createdAt = 1234567890L,
            updatedAt = 1234567890L
        )
        val id = userDao.insertUser(user)
        userDao.deactivateUser(id)
        val byId = userDao.getUserById(id)
        assertEquals(null, byId)
    }
}
