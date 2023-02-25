package pl.bk20.forest.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.core.data.source.SettingsStore
import pl.bk20.forest.core.domain.model.Settings
import pl.bk20.forest.core.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsStore: SettingsStore
) : SettingsRepository {

    override fun getSettings(): Flow<Settings> {
        return settingsStore.getSettings()
    }
}