package com.example.a99hub.data.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data  class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("client_id")
    val client_id: String,
    @SerializedName("username")
    val username: String,
)