package com.lasalle.spaceapps.data.local

import androidx.room.*
import com.lasalle.spaceapps.data.model.Rocket
import kotlinx.coroutines.flow.Flow

@Dao
interface RocketDao {
    @Query("SELECT * FROM rockets")
    fun getAllRockets(): List<Rocket>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRockets(rockets: List<Rocket>)

    @Query("DELETE FROM rockets")
    suspend fun deleteAllRockets()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rockets: List<Rocket>)

    @Query("SELECT * FROM rockets WHERE id = :rocketId LIMIT 1")
    suspend fun getRocketById(rocketId: String): Rocket?
}