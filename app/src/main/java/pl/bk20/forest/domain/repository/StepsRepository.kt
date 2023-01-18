package pl.bk20.forest.domain.repository

import pl.bk20.forest.domain.model.Steps
import java.time.LocalDate

interface StepsRepository {

    suspend fun getSteps(date: LocalDate): Steps?

    suspend fun insertSteps(steps: Steps)
}