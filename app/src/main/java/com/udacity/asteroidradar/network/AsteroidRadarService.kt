package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

enum class AsteroidApiFilter(val value: String) { SHOW_SAVED("saved"), SHOW_TODAY("today"), SHOW_WEEK("week") }

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

/**
 * A retrofit service to fetch the picture of the day.
 */
interface AsteroidRadarService {
    @GET(value = "planetary/apod")
    fun getPictureOfDay(@Query("api_key") apiKey: String): Deferred<NetworkPictureOfDay>

    @GET(value = "neo/rest/v1/feed")
    fun getAsteroids(@Query("api_key") apiKey: String) : Deferred<String>
}

/**
 * Main entry point for network access. Call like `Network.picturesOfDay.getPictureOfDay()`
 */
object Network {

    val radarApi = retrofit.create(AsteroidRadarService::class.java)
}