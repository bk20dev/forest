package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Settings
import pl.bk20.forest.domain.repository.SettingsRepository

class GetSettingsImpl(
    private val repository: SettingsRepository
) : GetSettings {

    override fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}