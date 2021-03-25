package com.example.a99hub.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.a99hub.model.database.Ledger
import com.example.a99hub.repository.LedgerRepository


class LedgerViewModel : ViewModel() {

    fun insert(context: Context, profile: List<Ledger>) {

        LedgerRepository.insert(context, profile)

    }

    fun getLedger(context: Context): LiveData<List<Ledger>>? {
        return LedgerRepository.getLedger(context)
    }

    fun update(context: Context, profile: Ledger) {
        LedgerRepository.update(context, profile)
    }

//    fun search(context: Context, data: String): LiveData<List<Ledger>>? {
//        return LedgerRepository.search(context, data)
//    }

    fun delete(context: Context, profile: Ledger) {
        LedgerRepository.delete(context, profile)
    }

    fun allDelete(context: Context) {
        LedgerRepository.allDelete(context)
    }


}