package com.example.a99hub.data.network

import com.example.a99hub.data.responses.ChangePassResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChangePassApi : BaseApi {

    @FormUrlEncoded
    @POST("update/password")
    fun changePass(
        @Field("token") token: String,
        @Field("old") old: String,
        @Field("new") new: String,
    ): ChangePassResponse
}