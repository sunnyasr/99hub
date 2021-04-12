package com.example.a99hub.ui.changepassword

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a99hub.data.network.Resource
import com.example.a99hub.data.repository.ChangePassRepository
import com.example.a99hub.data.responses.ChangePassResponse
import com.example.a99hub.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ChangePassViewModel @ViewModelInject constructor
    (private val repository: ChangePassRepository) :
    BaseViewModel(repository) {

    private val _changePassResponse: MutableLiveData<Resource<ChangePassResponse>> =
        MutableLiveData()
    val changePassResponse: LiveData<Resource<ChangePassResponse>>
        get() = _changePassResponse


    fun changePass(
        token: String,
        oldpass: String,
        newpass: String
    ) = viewModelScope.launch {
        _changePassResponse.value = Resource.Loading
        _changePassResponse.value = repository.changePass(token, oldpass, newpass)
    }
}