package pl.bk20.forest.stats.presentation

import java.time.LocalDate

data class StatsDetailsState(
    val date: LocalDate,
    val stepsTaken: Int,
    val treesCollected: Int,
    val calorieBurned: Int,
    val distanceTravelled: Double,
    val carbonDioxideSaved: Double,
    val chartDateRange: ClosedRange<LocalDate>
)