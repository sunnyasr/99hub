package com.example.a99hub.data.dataStore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.a99hub.data.responses.LimitResponse

class LimitManager(context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "limit_pref"
    )

    companion object {
        val LIMIT_KEY = preferencesKey<String>("key_limit")
        val LOCKED_KEY = preferencesKey<String>("key_locked")
        val HIDE_COMMISSION_KEY = preferencesKey<String>("key_hide_commission")
        val NEW_KEY = preferencesKey<String>("key_new")
        val NAME_KEY = preferencesKey<String>("key_name")
        val VALID_KEY = preferencesKey<String>("key_valid")
    }

    //    //Store user data
    suspend fun store(res: LimitResponse) {
        dataStore.edit {
            it[LIMIT_KEY] = res.current
            it[LOCKED_KEY] = res.locked
            it[HIDE_COMMISSION_KEY] = res.hide_commission
            it[NEW_KEY] = res.new
            it[NAME_KEY] = res.name
            it[VALID_KEY] = res.valid

        }
    }

    val coin: Flow<String>
        get() = dataStore.data
            .map { preferences ->
                preferences[LIMIT_KEY] ?: "0"
            }

}