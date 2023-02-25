package pl.bk20.forest.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.core.domain.model.Settings

interface GetSettings {

    operator fun invoke(): Flow<Settings>
}