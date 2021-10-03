package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PictureOfDayDao {
    @Query(NasaDatabase.GET_PICTURE_OF_DAY)
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(picture: DatabasePictureOfDay)
}

@Database(entities = [DatabasePictureOfDay::class], version = 1, exportSchema = false)
abstract class NasaDatabase : RoomDatabase() {
    /**
     * All queries are stored in this object
     * All constants related to the database are in this object
     */
    companion object {
        const val DATABASE_NAME = "NasaDatabase"
        const val GET_PICTURE_OF_DAY = "SELECT * FROM DatabasePictureOfDay pod ORDER BY pod.date DESC LIMIT 0,1"
    }
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: NasaDatabase

fun getDatabase(context: Context): NasaDatabase {
    synchronized(NasaDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                NasaDatabase::class.java, NasaDatabase.DATABASE_NAME).build()
        }
    }
    return INSTANCE
}