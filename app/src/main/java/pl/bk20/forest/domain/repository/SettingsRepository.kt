package pl.bk20.forest.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Settings

interface SettingsRepository {

    fun getSettings(): Flow<Settings>
}