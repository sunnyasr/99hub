package com.example.a99hub.network

import com.example.a99hub.model.UGModel
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import net.simplifiedcoding.data.responses.LimitResponse
import net.simplifiedcoding.data.responses.LoginResponse
import net.simplifiedcoding.data.responses.LogoutResponse
import net.simplifiedcoding.data.responses.ProfileResponse
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


    @GET("limit")
    fun getLimitCoins(@Query("token") token: String): Observable<List<LimitResponse>>

    @GET("limit")
    fun getBets(
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