package com.ismoil.vocabulary.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ismoil.vocabulary.room.dao.WordDao
import com.ismoil.vocabulary.room.entities.Word

@Database(entities = arrayOf(Word::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java, "word_db"
                    ).fallbackToDestructiveMigration().build()

                }
            }
            return INSTANCE
        }
    }

}

