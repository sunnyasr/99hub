package com.example.a99hub.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.a99hub.model.database.CompleteGame


@Dao
interface CGDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: CompleteGame)

    @Query("SELECT * FROM CGame")
    fun getCompleteGame(): LiveData<List<CompleteGame>>

    @Update
    suspend fun update(note: CompleteGame)

    @Delete
    suspend fun delete(note: CompleteGame)

    @Query("DELETE FROM CGame")
    suspend fun allDelete()


}