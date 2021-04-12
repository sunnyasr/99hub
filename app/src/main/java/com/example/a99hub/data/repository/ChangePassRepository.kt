package com.example.a99hub.data.repository

import com.example.a99hub.data.network.ChangePassApi
import javax.inject.Inject

class ChangePassRepository @Inject constructor(
    private val api: ChangePassApi
) : BaseRepository(api) {

    suspend fun changePass(
        token: String,
        oldpass: String,
        newpass: String
    ) = safeApiCall {
        api.changePass(token, oldpass, newpass)
    }

}