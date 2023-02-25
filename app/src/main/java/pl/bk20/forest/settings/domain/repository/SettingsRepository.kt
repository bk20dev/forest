package pl.bk20.forest.settings.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.settings.domain.model.Settings

interface SettingsRepository {

    fun getSettings(): Flow<Settings>
}