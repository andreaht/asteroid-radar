package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.PictureOfDay

@Entity
data class DatabasePictureOfTheDay constructor(
    @PrimaryKey
    val url : String,
    val mediaType : String,
    val title : String)

fun DatabasePictureOfTheDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(url, mediaType, title)
}