package com.example.a99hub.ui.ledger

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a99hub.data.network.Resource
import com.example.a99hub.data.repository.InPlayRepository
import com.example.a99hub.model.database.Ledger
import com.example.a99hub.data.repository.LedgerRepository
import com.example.a99hub.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody


class LedgerViewModel @ViewModelInject constructor(private val repository: LedgerRepository) :
    BaseViewModel(repository) {

    private val _ledgerResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()
    val ledgerResponse: LiveData<Resource<ResponseBody>>
        get() = _ledgerResponse

    fun getInPlay(token:String) = viewModelScope.launch {
        _ledgerResponse.value = Resource.Loading
        _ledgerResponse.value = repository.getLedger(token)
    }

    fun insert(context: Context, profile: List<Ledger>) {

        LedgerRepository.insert(context, profile)

    }

    fun getLedger(context: Context): LiveData<List<Ledger>>? {
        return LedgerRepository.getLedger(context)
    }

    fun update(context: Context, profile: Ledger) {
        LedgerRepository.update(context, profile)
    }

    fun delete(context: Context, profile: Ledger) {
        LedgerRepository.delete(context, profile)
    }

    fun allDelete(context: Context) {
        LedgerRepository.allDelete(context)
    }


}