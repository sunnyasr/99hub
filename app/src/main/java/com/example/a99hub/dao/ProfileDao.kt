package com.example.a99hub.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.a99hub.model.database.Profile

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Profile)

    @Query("SELECT * FROM profile ORDER BY id DESC")
    fun getCardsData(): LiveData<List<Profile>>

    @Update
    suspend fun update(note: Profile)

    @Query("SELECT * FROM profile WHERE username LIKE :username")
    fun search(username: String): LiveData<List<Profile>>


    @Delete
    suspend fun delete(note: Profile)
}