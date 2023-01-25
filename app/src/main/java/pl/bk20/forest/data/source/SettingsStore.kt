package pl.bk20.forest.data.source

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Settings

interface SettingsStore {

    fun getSettings(): Flow<Settings>
}