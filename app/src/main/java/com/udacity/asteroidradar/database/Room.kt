package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PictureOfTheDayDao {
    @Query("select * from databasePictureOfTheDay limit 1")
    fun getPicture(): LiveData<DatabasePictureOfTheDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( picture: DatabasePictureOfTheDay)
}

@Database(entities = [DatabasePictureOfTheDay::class], version = 1)
abstract class PicturesDatabase : RoomDatabase() {
    abstract val pictureDao: PictureOfTheDayDao
}

private lateinit var INSTANCE: PicturesDatabase

fun getDatabase(context: Context): PicturesDatabase {
    synchronized(PicturesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                PicturesDatabase::class.java,
                "videos").build()
        }
    }
    return INSTANCE
}