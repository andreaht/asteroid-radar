package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.database.DatabasePictureOfDay

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */


/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay(
    val url : String,
    val date : String,
    @Json(name = "media_type") val mediaType : String,
    val title : String)


/**
 * Convert Network results to database objects
 */
fun NetworkPictureOfDay.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        url,
        date,
        mediaType,
        title
    )
}
/*
@JsonClass(generateAdapter = true)
data class NetworkAsteroid constructor(
    val id: Long,
    @Json(name = "code_name")val codename: String,
    @Json(name = "date")val closeApproachDate: String,
    @Json(name = "absolute_magnitude")val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter_max")val estimatedDiameter: Double,
    @Json(name = "kilometers_per_second")val relativeVelocity: Double,
    @Json(name = "astronomical")val distanceFromEarth: Double,
    @Json(name = "is_potentially_hazardous_asteroid")val isPotentiallyHazardous: Boolean)
*/
fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}