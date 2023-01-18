package pl.bk20.forest.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Steps
import java.time.LocalDate

interface StepsRepository {

    fun getSteps(date: LocalDate): Flow<Steps?>

    suspend fun insertSteps(steps: Steps)
}