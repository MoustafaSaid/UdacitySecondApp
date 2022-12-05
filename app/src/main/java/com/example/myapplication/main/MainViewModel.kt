package com.example.myapplication.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Asteroid
import com.example.myapplication.Constants
import com.example.myapplication.Data.Local.database.AsteroidDB
import com.example.myapplication.Data.api.AsteroidApi
import com.example.myapplication.PictureOfDay
import com.example.myapplication.repos.AsteroidRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDB.getInstance(application)
     val asteroidRepository = AsteroidRepo(database)

    private val _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFragment: LiveData<Asteroid>
        get() = _navigateToDetailFragment

    val asteroids = asteroidRepository.asteroids


    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _isloading = MutableLiveData<Boolean>()
    val isloading: LiveData<Boolean>
        get() = _isloading

    init {
        viewModelScope.launch {
            try {

                asteroidRepository.refreshAsteroids()
                getPictureOfTheDay()

            } catch (e: Exception) {
                println("Exception refreshing data: $e.message")
                _isloading.value = false
            }
        }
    }

    // val asteroids = asteroidRepository.asteroids

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun doneNavigating() {
        _navigateToDetailFragment.value = null
    }

    fun doneDisplayingSnackbar() {
        _isloading.value = false
    }

    fun updateFilter(filter: AsteroidRepo.AsteroidFilter) {
        asteroidRepository.applyFilter(filter)
    }


    suspend fun getPictureOfTheDay(){
        _isloading.value = true
        withContext(Dispatchers.IO) {
            val pictureOfDay = AsteroidApi.retrofitService.getPictureOfDay(
                Constants.API_KEY
            )
                .await()

            _isloading.postValue(false)

            _pictureOfDay.postValue(pictureOfDay)

        }
    }

}
