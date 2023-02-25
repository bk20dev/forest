package pl.bk20.forest.progress.domain.usecase

import pl.bk20.forest.domain.repository.DayRepository
import pl.bk20.forest.domain.repository.SettingsRepository

class ProgressUseCases(
    dayRepository: DayRepository,
    settingsRepository: SettingsRepository
) {

    val getDay: GetDay = GetDayImpl(dayRepository, settingsRepository)
}