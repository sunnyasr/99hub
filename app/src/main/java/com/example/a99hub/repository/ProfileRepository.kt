package com.example.a99hub.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.a99hub.database.ProfileDatabase
import com.example.a99hub.model.database.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ProfileRepository {

    companion object {
        private var noteDatabase: ProfileDatabase? = null

        private fun initialiseDB(context: Context): ProfileDatabase? {
            return ProfileDatabase.getInstance(context)
        }

        fun insert(context: Context, note: Profile) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(IO).launch {
                noteDatabase?.getDao()?.insert(note)
            }
        }

        fun getCardData(context: Context): LiveData<List<Profile>>? {
            noteDatabase = initialiseDB(context)
            return noteDatabase?.getDao()?.getCardsData()
        }

        fun update(context: Context, note: Profile) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(IO).launch {
                noteDatabase?.getDao()?.update(note)
            }
        }

        fun search(context: Context, data: String): LiveData<List<Profile>>? {
            noteDatabase = initialiseDB(context)
            return noteDatabase?.getDao()?.search(data)
        }

        fun delete(context: Context, note: Profile) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(IO).launch {
                noteDatabase?.getDao()?.delete(note)
            }
        }
    }
}