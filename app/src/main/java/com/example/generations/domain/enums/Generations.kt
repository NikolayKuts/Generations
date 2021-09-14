package com.example.generations.domain.enums

enum class Generations(val range: IntRange, val generationName: String) {

    BABY_BOOM(1946..1964, "Baby boom"),
    X(1965..1980, "X / 13th Generation"),
    Y(1981..1996, "Y / Millennial"),
    Z(1997..2012, "Z / Homeland")
}