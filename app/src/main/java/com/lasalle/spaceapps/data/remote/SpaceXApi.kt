package com.lasalle.spaceapps.data.remote

import com.lasalle.spaceapps.data.model.Rocket
import retrofit2.http.GET

interface SpaceXApi {
    @GET("rockets")
    suspend fun getRockets(): List<Rocket>
}