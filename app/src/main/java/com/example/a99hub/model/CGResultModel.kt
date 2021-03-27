package com.example.a99hub.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class CGResultModel(
    val bet_type: String,
    val result: String,
    val market_id: String,
    val name: String,
    val start_time: String
) :
    Serializable