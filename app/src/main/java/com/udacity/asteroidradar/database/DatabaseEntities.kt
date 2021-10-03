package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.PictureOfDay

@Entity
data class DatabasePictureOfDay constructor(
    @PrimaryKey
    val url : String,
    val date: String,
    val mediaType : String,
    val title : String)

fun DatabasePictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(url, mediaType, title)
}