package com.example.a99hub.data.network

import com.example.a99hub.data.responses.LimitResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi :BaseApi {

    @GET("limit")
    suspend fun getLimitCoins(@Query("token") token: String): List<LimitResponse>
}