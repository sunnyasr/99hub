package com.example.a99hub.data.repository

import com.example.a99hub.data.network.BaseApi
import com.example.a99hub.data.network.Resource
import com.example.a99hub.data.network.SafeApiCall
import com.example.a99hub.data.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository(private val api: BaseApi) : SafeApiCall {

    suspend fun logout(token:String) = safeApiCall {
        api.logout(token)
    }
}