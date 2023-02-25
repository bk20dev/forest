package pl.bk20.forest.core.domain.model

data class StatsSummary(
    val treesCollected: Int = 0,
    val stepsTaken: Long = 0L,
    val calorieBurned: Double = 0.0,
    val distanceTravelled: Double = 0.0,
    val carbonDioxideSaved: Double = 0.0,
) {
    companion object
}

fun StatsSummary.Companion.of(days: List<Day>): StatsSummary {
    val treesCollected = days.count { it.steps >= it.goal }
    val stepsTaken = days.sumOf { it.steps.toLong() }
    val calorieBurned = days.sumOf { it.calorieBurned }
    val distanceTravelled = days.sumOf { it.distanceTravelled }
    val carbonDioxideSaved = days.sumOf { it.carbonDioxideSaved }
    return StatsSummary(
        treesCollected = treesCollected,
        stepsTaken = stepsTaken,
        calorieBurned = calorieBurned,
        distanceTravelled = distanceTravelled,
        carbonDioxideSaved = carbonDioxideSaved
    )
}