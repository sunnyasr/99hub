package com.example.a99hub.data.responses

import androidx.annotation.Keep

@Keep
data class ProfileResponse(
    val username: String,
    val name: String,
    val mobile: String,
    val created: String,
)