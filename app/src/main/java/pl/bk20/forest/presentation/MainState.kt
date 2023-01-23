package pl.bk20.forest.presentation

import java.time.LocalDate

data class MainState(
    val date: LocalDate,
    val takenSteps: Int,
    val dailyGoal: Int,
    val distanceTravelledInKm: Float,
    val calorieBurned: Int
)