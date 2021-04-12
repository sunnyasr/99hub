package com.example.a99hub.data.repository

import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.data.network.AuthApi
import com.example.a99hub.data.responses.LoginResponse
import javax.inject.Inject

class  AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val preferences: UserManager
) : BaseRepository(api) {

    suspend fun login(
        email: String,
        password: String,
        ip: String
    ) = safeApiCall {
        api.login(email, password, ip)
    }

    suspend fun saveAuthToken(token: LoginResponse) {
        preferences.storeUser(token)
    }

}