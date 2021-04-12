package com.example.a99hub.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a99hub.data.dao.*
import com.example.a99hub.model.database.*

@Database(
    entities = [CompleteGame::class, InPlayGame::class, UpcomingGame::class, Ledger::class],
    version = 9,
    exportSchema = false
)
abstract class ProfileDatabase : RoomDatabase() {


    abstract fun getCGDao(): CGDao
    abstract fun getInPlayDao(): InPlayDao
    abstract fun getUpcomingGameDao(): UGameDao
    abstract fun getLedgerDao(): LedgerDao

    companion object {
        private const val DATABASE_NAME = "ProfileDatabase"

        @Volatile
        var instance: ProfileDatabase? = null

        fun getInstance(context: Context): ProfileDatabase? {
            if (instance == null) {
                synchronized(ProfileDatabase::class.java)
                {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context, ProfileDatabase::class.java,
                            DATABASE_NAME
                        ).allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return instance
        }
    }

}