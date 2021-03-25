package com.example.a99hub.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ledger")
data class Ledger(
    @PrimaryKey
    val event_id: String,
    val market_id: String,
    val long_name: String,
    val short_name: String,
    val winner: String,
    val start_time: String,
    val transaction: String,
    val lost: String,
    val won: String,
    val balance: String,
) : Serializable