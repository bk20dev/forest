package pl.bk20.forest.domain.model

import java.time.LocalDate

data class DayParameters(

    val date: LocalDate,

    val goal: Int,

    val height: Int,

    val weight: Int,

    val stepLength: Int,

    val pace: Double
)
