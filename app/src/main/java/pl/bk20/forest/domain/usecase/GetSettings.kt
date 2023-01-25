package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Settings

interface GetSettings {

    operator fun invoke(): Flow<Settings>
}