package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.bk20.forest.domain.model.Day
import pl.bk20.forest.domain.repository.DayRepository
import pl.bk20.forest.domain.repository.SettingsRepository
import java.time.LocalDate

class GetDayImpl(
    private val dayRepository: DayRepository,
    private val settingsRepository: SettingsRepository,
) : GetDay {

    override fun invoke(date: LocalDate): Flow<Day> {
        val settingsFlow = settingsRepository.getSettings()
        val dayFlow = dayRepository.getDay(date)

        return settingsFlow.combine(dayFlow) { settings, day ->
            day ?: Day(
                date = date,
                steps = 0,
                goal = settings.dailyGoal,
                height = settings.height,
                weight = settings.weight,
                stepLength = settings.stepLength,
                pace = settings.pace
            )
        }
    }
}