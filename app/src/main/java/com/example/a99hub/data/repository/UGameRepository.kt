package com.example.a99hub.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.a99hub.data.dataStore.LimitManager
import com.example.a99hub.data.database.ProfileDatabase
import com.example.a99hub.data.network.HomeApi
import com.example.a99hub.data.network.InPlayApi
import com.example.a99hub.data.network.UGameApi
import com.example.a99hub.data.responses.LimitResponse
import com.example.a99hub.model.database.InPlayGame
import com.example.a99hub.model.database.UpcomingGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UGameRepository @Inject constructor(
    private val api: UGameApi,
) : BaseRepository(api) {

    suspend fun getUGame() = safeApiCall {
        api.getAllComingGame()
    }

    companion object {
        private var noteDatabase: ProfileDatabase? = null

        private fun initialiseDB(context: Context): ProfileDatabase? {
            return ProfileDatabase.getInstance(context)
        }

        fun insert(context: Context, note: List<UpcomingGame>) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                noteDatabase?.getUpcomingGameDao()?.insert(note)
            }
        }

        fun getUpcomingGame(context: Context): LiveData<List<UpcomingGame>>? {
            noteDatabase = initialiseDB(context)
            return noteDatabase?.getUpcomingGameDao()?.getUpcomingGame()
        }

        fun update(context: Context, note: UpcomingGame) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                noteDatabase?.getUpcomingGameDao()?.update(note)
            }
        }

//        fun search(context: Context, data: String): LiveData<List<Profile>>? {
//            noteDatabase = initialiseDB(context)
//            return noteDatabase?.getDao()?.search(data)
//        }

        fun delete(context: Context, note: UpcomingGame) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                noteDatabase?.getUpcomingGameDao()?.delete(note)
            }
        }

        fun allDelete(context: Context) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                noteDatabase?.getUpcomingGameDao()?.allDelete()
            }
        }

//        suspend fun getPost(token:String): List<UGModel>{
//            = RetrofitBuilder.api.getAllPost()
//        }

    }
}

