package com.example.a99hub.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.a99hub.model.database.Profile
import com.example.a99hub.repository.ProfileRepository

class ProfileViewModel : ViewModel() {

    fun insert(context: Context, profile: Profile) {
        ProfileRepository.insert(context, profile)
    }

    fun getCardsData(context: Context): LiveData<List<Profile>>?
    {
        return ProfileRepository.getCardData(context)
    }

    fun update(context: Context, profile: Profile) {
        ProfileRepository.update(context, profile)
    }

    fun search(context: Context, data: String): LiveData<List<Profile>>? {
        return ProfileRepository.search(context, data)
    }

    fun delete(context: Context, profile: Profile) {
        ProfileRepository.delete(context, profile)
    }
}