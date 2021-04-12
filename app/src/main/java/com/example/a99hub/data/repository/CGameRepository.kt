package com.example.a99hub.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.a99hub.data.dataStore.LimitManager
import com.example.a99hub.data.database.ProfileDatabase
import com.example.a99hub.data.network.CGameApi
import com.example.a99hub.data.network.HomeApi
import com.example.a99hub.data.network.InPlayApi
import com.example.a99hub.data.network.UGameApi
import com.example.a99hub.data.responses.LimitResponse
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.model.database.InPlayGame
import com.example.a99hub.model.database.UpcomingGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CGameRepository @Inject constructor(
    private val api: CGameApi,
) : BaseRepository(api) {

    suspend fun getCGame(token:String) = safeApiCall {
        api.getCompletedGames(token)
    }

    companion object {
        private var noteDatabase: ProfileDatabase? = null

        private fun initialiseDB(context: Context): ProfileDatabase? {
            return ProfileDatabase.getInstance(context)
        }

        fun insert(context: Context, note: ArrayList<CompleteGame>) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                noteDatabase?.getCGDao()?.insert(note)
            }
        }

        fun getCompleteGame(context: Context): LiveData<List<CompleteGame>>? {
            noteDatabase = initialiseDB(context)
            return noteDatabase?.getCGDao()?.getCompleteGame()
        }

        fun update(context: Context, note: CompleteGame) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                noteDatabase?.getCGDao()?.update(note)
            }
        }

        fun delete(context: Context, note: CompleteGame) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                noteDatabase?.getCGDao()?.delete(note)
            }
        }

        fun allDelete(context: Context) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                noteDatabase?.getCGDao()?.allDelete()
            }
        }

    }
}

