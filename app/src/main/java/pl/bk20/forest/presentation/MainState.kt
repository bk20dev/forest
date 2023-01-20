package pl.bk20.forest.presentation

import java.time.LocalDate

data class MainState(
    val date: LocalDate,
    val stepCount: Int,
    val dailyGoal: Int
)