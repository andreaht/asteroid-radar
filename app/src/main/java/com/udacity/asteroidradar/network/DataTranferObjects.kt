package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabasePictureOfTheDay
import com.udacity.asteroidradar.domain.PictureOfDay

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * VideoHolder holds a list of Videos.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "videos": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkPictureOfDayContainer(val pic: NetworkPictureOfTheDay)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkPictureOfTheDay(
    val url : String,
    @Json(name = "media_type") val mediaType : String,
    val title : String)

/**
 * Convert Network results to database objects
 */
fun NetworkPictureOfDayContainer.asDomainModel(): PictureOfDay {

    return PictureOfDay(
        pic.url,
        pic.mediaType,
        pic.title
    )
}

fun NetworkPictureOfDayContainer.asDatabaseModel(): DatabasePictureOfTheDay {
    return DatabasePictureOfTheDay(
        pic.url,
        pic.mediaType,
        pic.title
    )

}