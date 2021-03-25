package com.example.a99hub.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.a99hub.database.ProfileDatabase
import com.example.a99hub.model.database.InPlayGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class InPlayGameRepository {

    companion object {
        private var noteDatabase: ProfileDatabase? = null

        private fun initialiseDB(context: Context): ProfileDatabase? {
            return ProfileDatabase.getInstance(context)
        }

        fun insert(context: Context, note: ArrayList<InPlayGame>) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(IO).launch {
                noteDatabase?.getInPlayDao()?.insert(note)
            }
        }

        fun getInPlayGame(context: Context): LiveData<List<InPlayGame>>? {
            noteDatabase = initialiseDB(context)
            return noteDatabase?.getInPlayDao()?.getInPlayGame()
        }

        fun update(context: Context, note: InPlayGame) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(IO).launch {
                noteDatabase?.getInPlayDao()?.update(note)
            }
        }

//        fun search(context: Context, data: String): LiveData<List<Profile>>? {
//            noteDatabase = initialiseDB(context)
//            return noteDatabase?.getDao()?.search(data)
//        }

        fun delete(context: Context, note: InPlayGame) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(IO).launch {
                noteDatabase?.getInPlayDao()?.delete(note)
            }
        }

        fun allDelete(context: Context) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(IO).launch {
                noteDatabase?.getInPlayDao()?.allDelete()
            }
        }

//        suspend fun getPost(token:String): List<UGModel>{
//            = RetrofitBuilder.api.getAllPost()
//        }

    }
}