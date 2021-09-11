package com.example.generations.data.implementations

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.generations.data.databases.GenerationRoomDatabase
import com.example.generations.data.repositories.MainRepository
import com.example.generations.domain.pojo.Generation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepositoryRoomImp(context: Context) : MainRepository {

    private val database = GenerationRoomDatabase.getInstance(context)

    override suspend fun getGeneration(): LiveData<Generation> = withContext(Dispatchers.IO) {
        database.generationDao().getGeneration()
    }

    override suspend fun insertGeneration(generation: Generation) {
        withContext(Dispatchers.IO) { database.generationDao().insertGeneration(generation) }
    }
}