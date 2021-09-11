package com.example.generations.data.repositories

import androidx.lifecycle.LiveData
import com.example.generations.domain.pojo.Generation

interface MainRepository {

    suspend fun getGeneration(): LiveData<Generation>

    suspend fun insertGeneration(generation: Generation)
}