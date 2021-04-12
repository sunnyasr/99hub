package com.example.a99hub.data.network

import com.example.a99hub.data.responses.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApi :BaseApi {

    @GET("profile")
  suspend  fun getProfile(@Query("token") token: String): List<ProfileResponse>
}