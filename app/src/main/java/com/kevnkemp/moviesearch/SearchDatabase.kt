package com.kevnkemp.moviesearch

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Search::class), version = 1)
abstract class SearchDatabase : RoomDatabase() {

    abstract fun searchDoa() : SearchDao

    companion object {

        @Volatile
        private var INSTANCE: SearchDatabase? = null

        fun getDatabase(context: Context) : SearchDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchDatabase::class.java,
                    "search_database"
                    ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}