package com.example.a99hub.network

import com.example.a99hub.model.UGModel
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import net.simplifiedcoding.data.responses.LoginResponse
import net.simplifiedcoding.data.responses.LogoutResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("ip") ip: String,
    ): Call<LoginResponse>
//
//    @GET("upcoming/sports")
//    fun getAllComingGame(): Observable<List<UGModel>>

    @FormUrlEncoded
    @POST("logout")
    fun logout(
        @Field("token") token: String,
    ): Observable<LogoutResponse>


    @GET("upcoming/sports")
    fun getAllComingGame(): Call<ResponseBody>

    companion object {
        operator fun invoke(): Api {
            return Retrofit.Builder()
                .baseUrl("https://bluexch.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create()
        }
    }
}