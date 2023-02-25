package pl.bk20.forest.settings.domain.usecase

import pl.bk20.forest.core.domain.repository.DayRepository
import pl.bk20.forest.settings.domain.repository.SettingsRepository

class SettingsUseCases(
    settingsRepository: SettingsRepository,
    dayRepository: DayRepository,
) {

    val getSettings: GetSettings = GetSettingsImpl(settingsRepository)
    val updateDaySettings: UpdateDaySettings = UpdateDaySettingsImpl(dayRepository)
}