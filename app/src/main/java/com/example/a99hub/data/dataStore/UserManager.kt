package com.example.a99hub.data.dataStore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.a99hub.data.responses.LoginResponse
import javax.inject.Inject

class UserManager @Inject constructor(context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "user_pref"
    )

    companion object {
        val STATUS_KEY = preferencesKey<Boolean>("key_status")
        val USERNAME_KEY = preferencesKey<String>("key_username")
        val TOKEN_KEY = preferencesKey<String>("key_token")
        val CLIENT_ID_KEY = preferencesKey<String>("key_client_id")
    }

    //Store user data
    suspend fun storeUser(res: LoginResponse) {
        dataStore.edit {
            it[STATUS_KEY] = res.status
            it[USERNAME_KEY] = res.username
            it[TOKEN_KEY] = res.token
            it[CLIENT_ID_KEY] = res.client_id

        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    val token: Flow<String?>
        get() = dataStore.data
            .map { preferences ->
                preferences[TOKEN_KEY]
            }

    val username: Flow<String>
        get() = dataStore.data
            .map { preferences ->
                preferences[USERNAME_KEY] ?: ""
            }

    //
    val clietID: Flow<String>
        get() = dataStore.data
            .map { preferences ->
                preferences[CLIENT_ID_KEY] ?: ""
            }

}