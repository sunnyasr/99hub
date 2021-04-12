package com.example.a99hub.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.a99hub.data.database.ProfileDatabase
import com.example.a99hub.model.database.CompleteGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CompleteGameRepository {

    companion object {
        private var noteDatabase: ProfileDatabase? = null

        private fun initialiseDB(context: Context): ProfileDatabase? {
            return ProfileDatabase.getInstance(context)
        }

        fun insert(context: Context, note: ArrayList<CompleteGame>) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(IO).launch {
                noteDatabase?.getCGDao()?.insert(note)
            }
        }

        fun getCompleteGame(context: Context): LiveData<List<CompleteGame>>? {
            noteDatabase = initialiseDB(context)
            return noteDatabase?.getCGDao()?.getCompleteGame()
        }

        fun update(context: Context, note: CompleteGame) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(IO).launch {
                noteDatabase?.getCGDao()?.update(note)
            }
        }

//        fun search(context: Context, data: String): LiveData<List<Profile>>? {
//            noteDatabase = initialiseDB(context)
//            return noteDatabase?.getDao()?.search(data)
//        }

        fun delete(context: Context, note: CompleteGame) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(IO).launch {
                noteDatabase?.getCGDao()?.delete(note)
            }
        }

        fun allDelete(context: Context) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(IO).launch {
                noteDatabase?.getCGDao()?.allDelete()
            }
        }

//        suspend fun getPost(token:String): List<UGModel>{
//            = RetrofitBuilder.api.getAllPost()
//        }

    }
}