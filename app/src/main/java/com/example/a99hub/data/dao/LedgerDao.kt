package com.example.a99hub.data.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.a99hub.model.database.Ledger


@Dao
interface LedgerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ledger: List<Ledger>)

    @Query("SELECT * FROM ledger")
    fun getLedger(): LiveData<List<Ledger>>

    @Update
    suspend fun update(ledger: Ledger)

    @Delete
    suspend fun delete(ledger: Ledger)

    @Query("DELETE FROM ledger")
    suspend fun allDelete()


}