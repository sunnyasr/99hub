package com.example.a99hub.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "profile")
data class Profile(

    val username: String,
    val name: String,
    val mobile: String,
    val created: String,
    @PrimaryKey
    val id: Int
)