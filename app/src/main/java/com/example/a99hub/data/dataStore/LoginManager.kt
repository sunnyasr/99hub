package com.example.a99hub.data.dataStore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginManager(context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "login_pref"
    )

    companion object {
        val KEY_LOGGED = preferencesKey<Boolean>("key_Logged")
    }


    val isLogged: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_LOGGED]?:false
        }

    suspend fun setLogged(isLogged: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_LOGGED] = isLogged
        }
    }





}