package com.example.generations.domain.pojo

import com.example.generations.domain.enums.Generations.*

class GenerationDescriptionProvider(private val year: Int) {

    companion object {
        const val EMPTY = "empty"
    }

    fun getDescription(): String {
        return when (year) {
            in BABY_BOOM.range -> BABY_BOOM.generationName
            in X.range -> X.generationName
            in Y.range -> Y.generationName
            in Z.range -> Z.generationName
            else -> EMPTY
        }
    }

}