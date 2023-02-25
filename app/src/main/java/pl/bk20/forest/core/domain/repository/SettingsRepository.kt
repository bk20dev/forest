package pl.bk20.forest.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.core.domain.model.Settings

interface SettingsRepository {

    fun getSettings(): Flow<Settings>
}