package com.example.a99hub.ui.inplay

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a99hub.data.network.Resource
import com.example.a99hub.model.database.InPlayGame
import com.example.a99hub.data.repository.InPlayRepository
import com.example.a99hub.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class InPlayGameViewModel @ViewModelInject constructor(private val repository: InPlayRepository) :
    BaseViewModel(repository) {

    private val _InPlayResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()
    val inPlayResponse: LiveData<Resource<ResponseBody>>
        get() = _InPlayResponse

    fun getInPlay() = viewModelScope.launch {
        _InPlayResponse.value = Resource.Loading
        _InPlayResponse.value = repository.getInPlay()
    }

    fun insert(context: Context, profile: ArrayList<InPlayGame>) {
        InPlayRepository.insert(context, profile)
    }

    fun getInPlayGame(context: Context): LiveData<List<InPlayGame>>? {
        return InPlayRepository.getInPlayGame(context)
    }

    fun update(context: Context, profile: InPlayGame) {
        InPlayRepository.update(context, profile)
    }

    fun delete(context: Context, profile: InPlayGame) {
        InPlayRepository.delete(context, profile)
    }

    fun allDelete(context: Context) {
        InPlayRepository.allDelete(context)
    }


}