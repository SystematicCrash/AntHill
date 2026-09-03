package com.anthill.app.data.dao

import androidx.room.*
import com.anthill.app.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id AND is_active = 1")
    suspend fun getUserById(id: Long): User?

    @Query("SELECT * FROM users WHERE username = :username AND is_active = 1")
    suspend fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)
    
    @Query("UPDATE users SET is_active = 0 WHERE id = :id")
    suspend fun deactivateUser(id: Long)
}
