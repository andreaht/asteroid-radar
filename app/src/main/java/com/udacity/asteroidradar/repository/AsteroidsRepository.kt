package com.udacity.asteroidradar.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.udacity.asteroidradar.domain.Asteroid;
import com.udacity.asteroidradar.Constants;
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NasaDatabase;
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.Network;
import com.udacity.asteroidradar.network.asDatabaseModel

import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber;

class AsteroidsRepository(private val database:NasaDatabase) {
    /**
     * Asteroids that can be shown on the screen.
     */
    val asteroids: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidDao.getAllAsteroids()) {
        it.asDomainModel()
    }

    /**
     * Refresh the asteroids stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the pics for use, observe [asteroids]
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            // Try statement is used to catch Network Exceptions so the app does not
            // crash when attempting to load without a network connection
            try {
                val asteroids = Network.radarApi.getAsteroids(Constants.API_KEY).await()
                database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
