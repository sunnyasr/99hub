package com.example.a99hub.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a99hub.data.network.Resource
import com.example.a99hub.data.repository.HomeRepository
import com.example.a99hub.data.responses.LimitResponse
import com.example.a99hub.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor
    (private val repository: HomeRepository) :
    BaseViewModel(repository) {

    private val _limitResponse: MutableLiveData<Resource<List<LimitResponse>>> = MutableLiveData()
    val limitResponse: LiveData<Resource<List<LimitResponse>>>
        get() = _limitResponse

    fun getCoins(
        token: String
    ) = viewModelScope.launch {
        _limitResponse.value = Resource.Loading
        _limitResponse.value = repository.getCoins(token)
    }


}