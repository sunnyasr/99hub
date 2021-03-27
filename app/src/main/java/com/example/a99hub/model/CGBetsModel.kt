package com.example.a99hub.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class CGBetsModel(
    val bet_amount: String,
    val bet_type: String,
    val market_id: String,
    val size: String,
    val rate: String,
    val team: String,
    val action: String,
    val created: String,
    val client_id: String,
    val transaction_reference: String,
    val transaction_amount: String,
    val transaction_type: String,
    val name: String,
    val result: String,
    val start_time: String,
) : Serializable
//{
//    fun getRate(): String {
//        return rate
//    }
//
//
//    fun getAction(): String {
//
//        if (action.equals("1"))
//            return "LAGAI"
//        else
//
//            return "KHAI"
//    }
//
//    fun getBetAmount(): String {
//        return bet_amount
//    }
//
//    fun getTransAmount(): String {
//        return transaction_amount
//    }
//
//    fun getTeam(): String {
//        return team
//    }

//    fun getMode(): String {
//
//        if (type==1)
//            return "YES"
//        else
//
//            return "NOT"
//    }
//}