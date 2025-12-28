package com.lasalle.spaceapps.data.local

import androidx.room.*
import com.lasalle.spaceapps.data.model.Rocket
import kotlinx.coroutines.flow.Flow

@Dao
interface RocketDao {
    @Query("SELECT * FROM rockets")
    fun getAllRockets(): Flow<List<Rocket>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRockets(rockets: List<Rocket>)

    @Query("DELETE FROM rockets")
    suspend fun deleteAllRockets()

    @Query("SELECT * FROM rockets WHERE id = :rocketId")
    suspend fun getRocketById(rocketId: String): Rocket?
}