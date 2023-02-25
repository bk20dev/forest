package pl.bk20.forest.settings.data.source

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.settings.domain.model.Settings

interface SettingsStore {

    fun getSettings(): Flow<Settings>
}