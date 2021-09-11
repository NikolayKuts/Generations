package com.example.generations.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.generations.data.implementations.MainRepositoryRoomImp
import com.example.generations.data.repositories.MainRepository
import com.example.generations.domain.pojo.Generation
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MainRepository = MainRepositoryRoomImp(application)

    fun getDataSours(callback: (LiveData<Generation>) -> Unit) {
        viewModelScope.launch {
            val sours = repository.getGeneration()
            callback(sours)
        }
    }

    fun insetDateContainer(generation: Generation) {
        viewModelScope.launch { repository.insertGeneration(generation) }
    }
}