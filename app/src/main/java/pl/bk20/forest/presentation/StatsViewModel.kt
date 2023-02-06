package pl.bk20.forest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.data.repository.DayRepositoryImpl
import pl.bk20.forest.data.repository.SettingsRepositoryImpl
import pl.bk20.forest.domain.usecase.DayUseCases
import java.time.LocalDate
import kotlin.math.roundToInt

class StatsViewModel(
    private val dayUseCases: DayUseCases
) : ViewModel() {

    private val _day = MutableStateFlow(StatsState(LocalDate.MIN, 0, 0, 0f))
    val day: StateFlow<StatsState> = _day.asStateFlow()

    private var selectDateJob: Job? = null

    init {
        val today = LocalDate.now()
        selectDay(today)
    }

    fun selectDay(date: LocalDate) {
        selectDateJob?.cancel()
        selectDateJob = dayUseCases.getDay(date).onEach {
            _day.value = StatsState(
                date = it.date,
                stepsTaken = it.steps,
                calorieBurned = it.calorieBurned.roundToInt(),
                distanceTravelled = it.distanceTravelled
            )
        }.launchIn(viewModelScope)
    }

    companion object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication

            val dayDatabase = application.forestDatabase
            val dayRepository = DayRepositoryImpl(dayDatabase.dayDao)
            val settingsStore = application.settingsStore
            val settingsRepository = SettingsRepositoryImpl(settingsStore)
            val dayUseCases = DayUseCases(dayRepository, settingsRepository)

            return StatsViewModel(dayUseCases) as T
        }
    }
}