package com.example.a99hub.data.network


import okhttp3.ResponseBody
import retrofit2.http.GET

interface InPlayApi : BaseApi {

    @GET("inplay/sports")
    suspend fun getInPlay(): ResponseBody
}