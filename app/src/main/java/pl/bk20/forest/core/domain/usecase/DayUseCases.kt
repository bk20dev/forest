package pl.bk20.forest.core.domain.usecase

import pl.bk20.forest.core.domain.repository.DayRepository
import pl.bk20.forest.settings.domain.repository.SettingsRepository

class DayUseCases(
    dayRepository: DayRepository,
    settingsRepository: SettingsRepository
) {

    val getDay: GetDay = GetDayImpl(dayRepository, settingsRepository)
    val incrementStepCount: IncrementStepCount = IncrementStepCountImpl(dayRepository, getDay)
}