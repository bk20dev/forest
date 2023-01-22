package pl.bk20.forest.service

import java.time.LocalDate

data class StepCounterState(
    val date: LocalDate,
    val takenSteps: Int,
    val dailyGoal: Int,
    val distanceTravelledInKm: Float,
    val calorieBurned: Int
)