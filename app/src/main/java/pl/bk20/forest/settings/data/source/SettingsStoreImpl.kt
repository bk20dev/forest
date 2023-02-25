package pl.bk20.forest.settings.data.source

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.bk20.forest.settings.domain.model.Settings

class SettingsStoreImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsStore, OnSharedPreferenceChangeListener {

    private val settings: MutableStateFlow<Settings>

    init {
        val parsedSettings = parseSettings(sharedPreferences)
        settings = MutableStateFlow(parsedSettings)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun getSettings(): Flow<Settings> {
        return settings.asStateFlow()
    }

    private fun parseSettings(sharedPreferences: SharedPreferences): Settings =
        sharedPreferences.run {
            Settings(
                dailyGoal = getNumericString("daily_goal", 0),
                stepLength = getNumericString("step_length", 0),
                height = getNumericString("height", 0),
                weight = getNumericString("weight", 0),
                pace = getNumericString("pace", 0.0)
            )
        }

    private fun SharedPreferences.getNumericString(key: String, defaultValue: Int): Int =
        getString(key, "")?.toIntOrNull() ?: defaultValue

    private fun SharedPreferences.getNumericString(key: String, defaultValue: Double): Double =
        getString(key, "")?.toDoubleOrNull() ?: defaultValue

    override fun onSharedPreferenceChanged(
        updatedSharedPreferences: SharedPreferences?,
        key: String?
    ) {
        settings.value = parseSettings(sharedPreferences)
    }
}