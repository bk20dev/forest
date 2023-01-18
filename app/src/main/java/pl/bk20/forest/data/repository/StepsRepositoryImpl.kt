package pl.bk20.forest.data.repository

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.data.source.StepsDao
import pl.bk20.forest.domain.model.Steps
import pl.bk20.forest.domain.repository.StepsRepository
import java.time.LocalDate

class StepsRepositoryImpl(
    private val dao: StepsDao
) : StepsRepository {

    override fun getSteps(date: LocalDate): Flow<Steps?> {
        return dao.getSteps(date)
    }

    override suspend fun insertSteps(steps: Steps) {
        dao.insertSteps(steps)
    }
}