package com.example.generations.data.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.generations.data.daos.GenerationDao
import com.example.generations.domain.pojo.Generation

@Database(entities = [Generation::class], version = 1, exportSchema = false)
abstract class GenerationRoomDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "generations.db"
        private var database: GenerationRoomDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): GenerationRoomDatabase {
            return database ?: synchronized(LOCK) {
                val instance = Room.databaseBuilder(
                    context,
                    GenerationRoomDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database = instance
                instance
            }
        }
    }

    abstract fun generationDao(): GenerationDao
}