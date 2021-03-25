package com.example.a99hub.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.a99hub.model.database.UpcomingGame
import com.example.a99hub.repository.UpcomingGameRepository

class UpcomingGameViewModel : ViewModel() {

    fun insert(context: Context, profile: List<UpcomingGame>) {

        UpcomingGameRepository.insert(context, profile)

    }

    fun getUpcomingGame(context: Context): LiveData<List<UpcomingGame>>? {
        return UpcomingGameRepository.getUpcomingGame(context)
    }

    fun update(context: Context, profile: UpcomingGame) {
        UpcomingGameRepository.update(context, profile)
    }

//    fun search(context: Context, data: String): LiveData<List<UpcomingGame>>? {
//        return UpcomingGameRepository.search(context, data)
//    }

    fun delete(context: Context, profile: UpcomingGame) {
        UpcomingGameRepository.delete(context, profile)
    }

    fun allDelete(context: Context) {
        UpcomingGameRepository.allDelete(context)
    }


}