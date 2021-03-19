package com.example.a99hub.data.sharedprefrence

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class Token(ctx: Context) {

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    private val PREF_NAME: String = "99hub_app"
    private val PRIVATE_MODE: Int = 0
    private val PREF_TOKEN: String = "99hub_token"
    private val PREF_USERNAME: String = "99hub_username"

    init {
        pref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun setToken(token: String) {
        editor.putString(PREF_TOKEN, token)
        editor.commit()
    }

    fun getToken(): String {
        return pref.getString(PREF_TOKEN, "").toString()
    }

    /*--------------------------------------------------------*/
    fun setUsername(username: String) {
        editor.putString(PREF_USERNAME, username)
        editor.commit()
    }

    fun getUsername(): String {
        return pref.getString(PREF_USERNAME, "").toString()
    }
}