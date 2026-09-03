package com.anthill.app.data.repository

import com.anthill.app.data.dao.UserDao
import com.anthill.app.data.model.User

class AuthRepository(private val userDao: UserDao) {
    suspend fun login(username: String, passwordHash: String): User? {
        val user = userDao.getUserByUsername(username)
        return if (user?.passwordHash == passwordHash) user else null
    }
}
