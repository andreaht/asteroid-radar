package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.PicturesDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.PictureOfDayApi
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PicturesOfDayRepository(private val database: PicturesDatabase) {
    /**
     * Picture Of The Day that can be shown on the screen.
     */
    val picture: LiveData<PictureOfDay> =
        Transformations.map(database.pictureDao.getPicture()) {
            it?.asDomainModel()
        }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [videos]
     */
    suspend fun refreshPictures() {
        withContext(Dispatchers.IO) {
            val picturesOfDay = PictureOfDayApi.picturesOfDay.getPictureOfDay().await()
            //TODO remove before insert new ones
            database.pictureDao.insertAll(picturesOfDay.asDatabaseModel())
        }
    }
}