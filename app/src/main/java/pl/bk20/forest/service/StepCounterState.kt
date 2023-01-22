package pl.bk20.forest.service

data class StepCounterState(
    val takenSteps: Int,
    val dailyGoal: Int,
    val distanceTravelledInKm: Float,
    val calorieBurned: Int
)