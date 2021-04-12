package com.example.a99hub.data.network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import com.example.a99hub.data.responses.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("ip") ip: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("update/password")
    fun changePass(
        @Field("token") token: String,
        @Field("old") old: String,
        @Field("new") new: String,
    ): Observable<ChangePassResponse>


    @GET("limit")
    fun getLimitCoins(@Query("token") token: String): Observable<List<LimitResponse>>

    @GET("completed/sports")
    fun getCompletedGames(@Query("token") token: String): Observable<ResponseBody>

    @GET("limit")
    fun getBets(
        @Query("token") token: String,
        @Query("event_id") event_id: String
    ): Observable<ResponseBody>

    @GET("bets")
    fun getCompletedBets(
        @Query("token") token: String,
        @Query("event_id") event_id: String
    ): Observable<ResponseBody>

    @GET("profile")
    fun getProfile(@Query("token") token: String): Observable<List<ProfileResponse>>

    @FormUrlEncoded
    @POST("logout")
    fun logout(
        @Field("token") token: String,
    ): Observable<LogoutResponse>


    @GET("upcoming/sports")
    fun getAllComingGame(): Call<ResponseBody>

    @GET("inplay/sports")
    fun getInPlay(): Observable<ResponseBody>


    @GET("ledger")
    fun getLedger(
        @Query("token") token: String,
    ): Call<ResponseBody>

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