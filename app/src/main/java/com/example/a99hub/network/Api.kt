package com.example.a99hub.network

import net.simplifiedcoding.data.responses.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("login")
    fun uswrLogin(
        @Field("username") username: String,
        @Field("ap") password: String,
        @Field("ap") ip: String,
    ): Call<LoginResponse>

    companion object {
        operator fun invoke(): Api {
            return Retrofit.Builder()
                .baseUrl("https://bluexch.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create()
        }
    }
}