package com.example.a99hub.data.repository


import com.example.a99hub.data.network.ProfileApi
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val api: ProfileApi
) : BaseRepository(api) {

    suspend fun getProfile(token: String) = safeApiCall {
        api.getProfile(token)
    }


}