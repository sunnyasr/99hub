package com.example.a99hub.data.network


import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface CGameApi : BaseApi {
    @GET("completed/sports")
    suspend fun getCompletedGames(@Query("token") token: String): ResponseBody
}