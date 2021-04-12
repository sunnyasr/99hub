package com.example.a99hub.data.network

import com.example.a99hub.data.responses.LoginResponse

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi : BaseApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("ip") ip: String
    ): LoginResponse
}