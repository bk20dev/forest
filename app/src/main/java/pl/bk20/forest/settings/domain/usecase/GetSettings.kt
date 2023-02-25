package pl.bk20.forest.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.settings.domain.model.Settings
import pl.bk20.forest.settings.domain.repository.SettingsRepository

interface GetSettings {

    operator fun invoke(): Flow<Settings>
}

class GetSettingsImpl(
    private val repository: SettingsRepository
) : GetSettings {

    override fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}