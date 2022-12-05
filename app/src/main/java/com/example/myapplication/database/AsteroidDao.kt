package com.example.myapplication.Data.Local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.Asteroid


/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidTable)

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate <= :startDate AND closeApproachDate >= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByDate(startDate: String, endDate: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid_table WHERE closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int

}

