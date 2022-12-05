package com.example.myapplication.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.myapplication.Asteroid
import com.example.myapplication.Constants
import com.example.myapplication.Data.Local.database.AsteroidDB
import com.example.myapplication.Data.Local.database.asDomainModel
import com.example.myapplication.Data.api.AsteroidApi.retrofitService
import com.example.myapplication.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class AsteroidRepo(val database: AsteroidDB) {

    enum class AsteroidFilter(val value: String) { SAVED("saved"), TODAY("today"), WEEK("week") }

    private val _asteroidType: MutableLiveData<AsteroidFilter> =
        MutableLiveData(AsteroidFilter.WEEK)
    private val asteroidType: LiveData<AsteroidFilter>
        get() = _asteroidType


    val asteroids: LiveData<List<Asteroid>> =
        Transformations.switchMap(asteroidType) { type ->
            when (type) {
                AsteroidFilter.SAVED -> database.sleepDao.getAllAsteroids()
                AsteroidFilter.TODAY -> database.sleepDao.getAsteroidsByDate(
                    getToday(),
                    getToday()
                )
                AsteroidFilter.WEEK -> database.sleepDao.getAsteroidsByDate(
                    getSeventhDay(),
                    getNextDay()
                )
                else -> throw IllegalArgumentException(" Invalid type !")
            }
        }

    suspend fun refreshAsteroids(
        startDate: String = "",
        endDate: String = ""
    ) {
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = retrofitService.getAsteroidsList(
                startDate, endDate,
                Constants.API_KEY
            )
                .await()
            asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))
            database.sleepDao.insertAll(*asteroidList.asDomainModel())
        }
    }

    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO) {
            database.sleepDao.deletePreviousDayAsteroids(getPreviousDay())
        }
    }

    fun applyFilter(filter: AsteroidFilter) {

        _asteroidType.value = filter
    }

}