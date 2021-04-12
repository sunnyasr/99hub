package com.example.a99hub.data.network

import okhttp3.ResponseBody
import retrofit2.http.GET

interface UGameApi : BaseApi {

    @GET("upcoming/sports")
    suspend fun getAllComingGame(): ResponseBody
}