package pl.bk20.forest.core.data.source

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.core.domain.model.Settings

interface SettingsStore {

    fun getSettings(): Flow<Settings>
}