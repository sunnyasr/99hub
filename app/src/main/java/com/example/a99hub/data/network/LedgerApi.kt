package com.example.a99hub.data.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface LedgerApi : BaseApi {
    @GET("ledger")
    suspend fun getLedger(
        @Query("token") token: String,
    ): ResponseBody
}