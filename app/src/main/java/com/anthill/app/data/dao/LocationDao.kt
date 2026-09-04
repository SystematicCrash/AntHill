package com.anthill.app.data.dao

import androidx.room.*
import com.anthill.app.data.model.Location

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocation(location: Location): Long

    @Query("SELECT * FROM locations WHERE id = :id AND is_active = 1")
    suspend fun getLocationById(id: Long): Location?

    @Query("SELECT * FROM locations WHERE parent_id = :parentId AND is_active = 1")
    suspend fun getChildren(parentId: Long): List<Location>
    
    @Query("SELECT * FROM locations WHERE parent_id IS NULL AND is_active = 1")
    suspend fun getRootLocations(): List<Location>
}
