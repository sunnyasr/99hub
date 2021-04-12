package com.example.a99hub.ui.base

import androidx.lifecycle.ViewModel
import com.example.a99hub.data.network.UserApi
import com.example.a99hub.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    suspend fun logout(token: String) = withContext(Dispatchers.IO) { repository.logout(token) }

}