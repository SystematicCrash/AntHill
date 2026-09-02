package com.anthill.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anthill.app.data.model.User
import com.anthill.app.data.model.Role
import com.anthill.app.data.model.Permission
import com.anthill.app.data.model.UserRoleCrossRef
import com.anthill.app.data.dao.UserDao

@Database(entities = [User::class, Role::class, Permission::class, UserRoleCrossRef::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
