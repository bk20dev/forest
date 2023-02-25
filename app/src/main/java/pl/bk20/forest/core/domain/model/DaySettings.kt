package pl.bk20.forest.core.domain.model

import java.time.LocalDate

data class DaySettings(

    val date: LocalDate,

    val goal: Int,

    val height: Int,

    val weight: Int,

    val stepLength: Int,

    val pace: Double
)
