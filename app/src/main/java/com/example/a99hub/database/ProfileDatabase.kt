package com.example.a99hub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a99hub.dao.CGDao
import com.example.a99hub.dao.ProfileDao
import com.example.a99hub.model.UGModel
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.model.database.Profile

@Database(entities = [Profile::class, CompleteGame::class], version = 2, exportSchema = false)
abstract class ProfileDatabase : RoomDatabase() {

    abstract fun getDao(): ProfileDao
    abstract fun getCGDao(): CGDao

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
                        ).build()
                    }
                }
            }
            return instance
        }
    }
}