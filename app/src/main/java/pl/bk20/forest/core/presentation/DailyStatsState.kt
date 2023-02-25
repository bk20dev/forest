package pl.bk20.forest.core.presentation

import java.time.LocalDate

data class DailyStatsState(
    val date: LocalDate,
    val stepsTaken: Int,
    val treeCollected: Boolean,
    val calorieBurned: Int,
    val distanceTravelled: Double,
    val carbonDioxideSaved: Double,
)