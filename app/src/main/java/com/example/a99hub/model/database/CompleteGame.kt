package com.example.a99hub.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "CGame")
data class CompleteGame(
    val sport_id: String,
    val sport_name: String,
    val sport_picture: String,
    @PrimaryKey
    val event_id: Int,
    val market_id: String,
    val long_name: String,
    val short_name: String,
    val start_time: String,
    val competition_name: String,
    val display_picture: String,
    val inactive: String
) : Serializable 
