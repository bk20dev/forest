package pl.bk20.forest.data.repository

import pl.bk20.forest.data.source.StepsDao
import pl.bk20.forest.domain.model.Steps
import pl.bk20.forest.domain.repository.StepsRepository
import java.time.LocalDate

class StepsRepositoryImpl(
    private val dao: StepsDao
) : StepsRepository {

    override suspend fun getSteps(date: LocalDate): Steps? {
        return dao.getSteps(date)
    }

    override suspend fun insertSteps(steps: Steps) {
        dao.insertSteps(steps)
    }
}