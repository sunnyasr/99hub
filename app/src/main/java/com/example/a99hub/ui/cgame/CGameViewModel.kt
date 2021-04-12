package com.example.a99hub.ui.cgame

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a99hub.data.network.Resource
import com.example.a99hub.data.repository.CGameRepository
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.data.repository.CompleteGameRepository
import com.example.a99hub.data.repository.UGameRepository
import com.example.a99hub.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class CGameViewModel  @ViewModelInject constructor(private val repository: CGameRepository) :
    BaseViewModel(repository) {

    private val _cGameResponse: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()
    val cGameResponseResponse: LiveData<Resource<ResponseBody>>
        get() = _cGameResponse

    fun getUGame(token:String) = viewModelScope.launch {
        _cGameResponse.value = Resource.Loading
        _cGameResponse.value = repository.getCGame(token)
    }

    fun insert(context: Context, profile: ArrayList<CompleteGame>) {
        CompleteGameRepository.insert(context, profile)
    }

    fun getCompleteGame(context: Context): LiveData<List<CompleteGame>>? {
        return CompleteGameRepository.getCompleteGame(context)
    }

    fun update(context: Context, profile: CompleteGame) {
        CompleteGameRepository.update(context, profile)
    }

    fun delete(context: Context, profile: CompleteGame) {
        CompleteGameRepository.delete(context, profile)
    }

    fun allDelete(context: Context) {
        CompleteGameRepository.allDelete(context)
    }


}