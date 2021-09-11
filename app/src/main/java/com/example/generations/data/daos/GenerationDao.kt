package com.example.generations.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.generations.domain.pojo.Generation
import com.example.generations.domain.pojo.GENERATION_TABLE_NAME

@Dao
interface GenerationDao {

    @Query("SELECT * FROM $GENERATION_TABLE_NAME")
    fun getGeneration(): LiveData<Generation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeneration(generation: Generation)
}