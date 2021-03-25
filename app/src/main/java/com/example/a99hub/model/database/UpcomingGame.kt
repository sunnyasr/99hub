package com.example.a99hub.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "UGame")
data class UpcomingGame(
    val sport_id: String,
    val sport_name: String,
    @PrimaryKey
    val event_id: String,
    val market_id: String,
    val long_name: String,
    val start_time: String,
    val inactive: String
) : Serializable 
