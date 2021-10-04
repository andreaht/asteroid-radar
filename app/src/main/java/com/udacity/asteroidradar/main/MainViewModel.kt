package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.AsteroidApiFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PicturesOfDayRepository
import kotlinx.coroutines.launch

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during activty
 * or fragment lifecycle events.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val filter = MutableLiveData(AsteroidApiFilter.SHOW_SAVED)
    private val picturesOfDayRepository = PicturesOfDayRepository(database)
    private val asteroidsRepository = AsteroidsRepository(database)
    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        viewModelScope.launch {
            picturesOfDayRepository.refreshPictureOfDay()
            asteroidsRepository.refreshAsteroids()
        }
    }

    val picOfDay = picturesOfDayRepository.pictureOfDay
    val asteroids = Transformations.switchMap(filter){
        when (it) {
            AsteroidApiFilter.SHOW_TODAY -> asteroidsRepository.asteroidsToday
            AsteroidApiFilter.SHOW_WEEK -> asteroidsRepository.asteroidsWeek
            else -> asteroidsRepository.asteroidsSaved
        }
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedProperty: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    /**
     * When the asteroid is clicked, set the [_navigateToSelectedAsteroid] [MutableLiveData]
     * @param marsProperty The [MarsProperty] that was clicked on.
     */
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * Updates the data set filter for the web services by querying the data with the new filter
     * by calling [getAsteroids]
     * @param filter the [AsteroidApiFilter] that is sent as part of the web server request
     */
    fun updateFilter(filter: AsteroidApiFilter) {
        this.filter.value = filter
    }

}