package com.example.a99hub.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.model.database.InPlayGame

@Dao
interface InPlayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: ArrayList<InPlayGame>)

    @Query("SELECT * FROM inPlayGame")
    fun getInPlayGame(): LiveData<List<InPlayGame>>

    @Update
    suspend fun update(note: InPlayGame)

    @Delete
    suspend fun delete(note: InPlayGame)

    @Query("DELETE FROM inPlayGame")
    suspend fun allDelete()


}