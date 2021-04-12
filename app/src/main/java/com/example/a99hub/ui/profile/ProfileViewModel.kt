package com.example.a99hub.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a99hub.data.network.Resource
import com.example.a99hub.data.repository.ProfileRepository
import com.example.a99hub.data.responses.ProfileResponse
import com.example.a99hub.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel  @ViewModelInject constructor(private val repository: ProfileRepository) :
    BaseViewModel(repository) {

    private val _profileResponse: MutableLiveData<Resource<List<ProfileResponse>>> = MutableLiveData()
    val profileResponse: LiveData<Resource<List<ProfileResponse>>>
        get() = _profileResponse

    fun getProfile(token:String) = viewModelScope.launch {
        _profileResponse.value = Resource.Loading
        _profileResponse.value = repository.getProfile(token)
    }


}