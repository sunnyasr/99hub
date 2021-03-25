package com.example.a99hub.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a99hub.model.database.InPlayGame
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.repository.CompleteGameRepository

class CompleteGameViewModel : ViewModel() {

    fun insert(context: Context, profile: ArrayList<CompleteGame>) {
        CompleteGameRepository.insert(context, profile)
    }

    fun getCompleteGame(context: Context): LiveData<List<CompleteGame>>? {
        return CompleteGameRepository.getCompleteGame(context)
    }

    fun update(context: Context, profile: CompleteGame) {
        CompleteGameRepository.update(context, profile)
    }

//    fun search(context: Context, data: String): LiveData<List<CompleteGame>>? {
//        return CompleteGameRepository.search(context, data)
//    }

    fun delete(context: Context, profile: CompleteGame) {
        CompleteGameRepository.delete(context, profile)
    }

    fun allDelete(context: Context) {
        CompleteGameRepository.allDelete(context)
    }


}