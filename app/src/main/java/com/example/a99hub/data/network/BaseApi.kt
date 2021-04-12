package com.example.a99hub.data.network

import com.example.a99hub.data.responses.LogoutResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BaseApi {
    @FormUrlEncoded
    @POST("logout")
    fun logout(
        @Field("token") token: String,
    ): LogoutResponse
}