package com.example.a99hub.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.a99hub.model.database.InPlayGame
import com.example.a99hub.repository.InPlayGameRepository

class InPlayGameViewModel : ViewModel() {

    fun insert(context: Context, profile: ArrayList<InPlayGame>) {
        InPlayGameRepository.insert(context, profile)
    }

    fun getInPlayGame(context: Context): LiveData<List<InPlayGame>>? {
        return InPlayGameRepository.getInPlayGame(context)
    }

    fun update(context: Context, profile: InPlayGame) {
        InPlayGameRepository.update(context, profile)
    }

//    fun search(context: Context, data: String): LiveData<List<InPlayGame>>? {
//        return InPlayGameRepository.search(context, data)
//    }

    fun delete(context: Context, profile: InPlayGame) {
        InPlayGameRepository.delete(context, profile)
    }

    fun allDelete(context: Context) {
        InPlayGameRepository.allDelete(context)
    }


}