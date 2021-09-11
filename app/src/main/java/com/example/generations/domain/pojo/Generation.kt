package com.example.generations.domain.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

const val GENERATION_TABLE_NAME = "generation"

@Entity
data class Generation(
    val name: String,
    @PrimaryKey val id: Int = 0,
)