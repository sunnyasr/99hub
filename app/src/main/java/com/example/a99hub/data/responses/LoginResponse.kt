package net.simplifiedcoding.data.responses

data  class LoginResponse(
    val token: String,
    val status: Boolean,
    val client_id: String,
    val username: String,
)