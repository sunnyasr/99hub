package com.example.a99hub.ui.ugame

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a99hub.data.network.Resource
import com.example.a99hub.data.repository.UGameRepository
import com.example.a99hub.model.database.UpcomingGame
import com.example.a99hub.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class UGameViewModel @ViewModelInject constructor(private val repository: UGameRepository) :
    BaseViewModel(repository) {

    private val _uGameResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()
    val uGameResponseResponse: LiveData<Resource<ResponseBody>>
        get() = _uGameResponse

    fun getUGame() = viewModelScope.launch {
        _uGameResponse.value = Resource.Loading
        _uGameResponse.value = repository.getUGame()
    }

    fun insert(context: Context, profile: List<UpcomingGame>) {
        UGameRepository.insert(context, profile)
    }

    fun getUpcomingGame(context: Context): LiveData<List<UpcomingGame>>? {
        return UGameRepository.getUpcomingGame(context)
    }

    fun update(context: Context, profile: UpcomingGame) {
        UGameRepository.update(context, profile)
    }

    fun delete(context: Context, profile: UpcomingGame) {
        UGameRepository.delete(context, profile)
    }

    fun allDelete(context: Context) {
        UGameRepository.allDelete(context)
    }
}