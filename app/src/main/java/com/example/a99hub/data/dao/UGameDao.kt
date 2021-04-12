package com.example.a99hub.data.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.a99hub.model.database.UpcomingGame


@Dao
interface UGameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: List<UpcomingGame>)

    @Query("SELECT * FROM UGame")
    fun getUpcomingGame(): LiveData<List<UpcomingGame>>

    @Update
    suspend fun update(note: UpcomingGame)

    @Delete
    suspend fun delete(note: UpcomingGame)

    @Query("DELETE FROM UGame")
    suspend fun allDelete()


}