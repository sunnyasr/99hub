package com.example.a99hub.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a99hub.data.repository.AuthRepository
import com.example.a99hub.data.repository.BaseRepository
import com.example.a99hub.ui.auth.AuthViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
//            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as HomeRepository) as T
//            modelClass.isAssignableFrom(BottomViewModel::class.java) -> BottomViewModel(repository as BottomSheetRepository) as T
//            modelClass.isAssignableFrom(LedgerViewModel::class.java) -> LedgerViewModel(repository as LedgerRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }

}