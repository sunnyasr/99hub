package com.example.a99hub.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.a99hub.data.database.ProfileDatabase
import com.example.a99hub.data.network.LedgerApi
import com.example.a99hub.data.network.ProfileApi
import com.example.a99hub.model.database.Ledger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class LedgerRepository @Inject constructor(
    private val api: LedgerApi
) : BaseRepository(api) {

    suspend fun getLedger(token: String) = safeApiCall {
        api.getLedger(token)
    }

    companion object {
        private var noteDatabase: ProfileDatabase? = null

        private fun initialiseDB(context: Context): ProfileDatabase? {
            return ProfileDatabase.getInstance(context)
        }

        fun insert(context: Context, note: List<Ledger>) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(IO).launch {
                noteDatabase?.getLedgerDao()?.insert(note)
            }
        }

        fun getLedger(context: Context): LiveData<List<Ledger>>? {
            noteDatabase = initialiseDB(context)
            return noteDatabase?.getLedgerDao()?.getLedger()
        }

        fun update(context: Context, note: Ledger) {
            noteDatabase = initialiseDB(context)

            CoroutineScope(IO).launch {
                noteDatabase?.getLedgerDao()?.update(note)
            }
        }

        fun delete(context: Context, note: Ledger) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(IO).launch {
                noteDatabase?.getLedgerDao()?.delete(note)
            }
        }

        fun allDelete(context: Context) {
            noteDatabase = initialiseDB(context)
            CoroutineScope(IO).launch {
                noteDatabase?.getLedgerDao()?.allDelete()
            }
        }

    }
}