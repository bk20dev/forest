package pl.bk20.forest.presentation

import java.time.LocalDate

data class DailyStatsState(
    val date: LocalDate,
    val stepsTaken: Int,
    val treeCollected: Boolean,
    val calorieBurned: Int,
    val distanceTravelled: Float,
    val carbonDioxideSaved: Float,
)