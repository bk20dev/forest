package pl.bk20.forest.presentation

import java.time.LocalDate

data class ProgressState(
    val date: LocalDate,
    val stepsTaken: Int,
    val dailyGoal: Int,
    val calorieBurned: Int,
    val distanceTravelled: Float,
    val carbonDioxideSaved: Float,
)