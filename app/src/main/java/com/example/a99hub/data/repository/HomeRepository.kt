package com.example.a99hub.data.repository

import com.example.a99hub.data.dataStore.LimitManager
import com.example.a99hub.data.network.HomeApi
import com.example.a99hub.data.responses.LimitResponse
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: HomeApi,
    private val preferences: LimitManager
) : BaseRepository(api) {

    suspend fun getCoins(
        token: String
    ) = safeApiCall {
        api.getLimitCoins(token)
    }

    suspend fun saveCoins(token: LimitResponse) {
        preferences.store(token)
    }





}

