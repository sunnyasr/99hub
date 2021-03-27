package com.example.a99hub.data.dataStore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.a99hub.data.responses.ProfileResponse

class ProfileManager(context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "profile_pref"
    )

    companion object {
        val NAME_KEY = preferencesKey<String>("key_name")
        val USERNAME_KEY = preferencesKey<String>("key_username")
        val MOBILE_KEY = preferencesKey<String>("key_mobile")
        val CREATED_KEY = preferencesKey<String>("key_created")
    }

    //    //Store user data
    suspend fun storeUser(res: ProfileResponse) {
        dataStore.edit {
            it[NAME_KEY] = res.name
            it[USERNAME_KEY] = res.username
            it[MOBILE_KEY] = res.mobile
            it[CREATED_KEY] = res.created

        }
    }

    val name: Flow<String>
        get() = dataStore.data
            .map { preferences ->
                preferences[NAME_KEY] ?: ""
            }

    val username: Flow<String>
        get() = dataStore.data
            .map { preferences ->
                preferences[USERNAME_KEY] ?: ""
            }

    val mobile: Flow<String>
        get() = dataStore.data
            .map { preferences ->
                preferences[MOBILE_KEY] ?: ""
            }

    val joinDate: Flow<String>
        get() = dataStore.data
            .map { preferences ->
                preferences[CREATED_KEY] ?: ""
            }

}