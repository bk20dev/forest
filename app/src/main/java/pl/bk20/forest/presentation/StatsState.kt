package pl.bk20.forest.presentation

import java.time.LocalDate

data class StatsState(
    val date: LocalDate,
    val stepsTaken: Int,
    val calorieBurned: Int,
    val distanceTravelled: Float
)