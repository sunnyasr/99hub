package com.example.a99hub.data.network

import com.example.a99hub.data.responses.LoginResponse
import com.example.a99hub.data.responses.LogoutResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @GET("user")
    suspend fun getUser(): LoginResponse

    @FormUrlEncoded
    @POST("logout")
    suspend fun logout(
        @Field("token") token: String,
    ): LogoutResponse

}