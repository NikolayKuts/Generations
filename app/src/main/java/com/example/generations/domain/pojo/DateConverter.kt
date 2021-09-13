package com.example.generations.domain.pojo

class DateConverter (
    private val year: Int,
    private val month: Int,
    private val day: Int,
) {
    private companion object {
        private const val ZERO = "0"
    }

    fun getDateAsString(): String {
        val month =  if (month in 1..9) "$ZERO$month" else month.toString()
        val day = if (day in 1..9) "$ZERO$day" else day.toString()
        return "$day/$month/$year"
    }
}