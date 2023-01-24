package pl.bk20.forest.presentation

import java.time.LocalDate

data class ProgressState(
    val date: LocalDate,
    val steps: Int,
    val goal: Int
)