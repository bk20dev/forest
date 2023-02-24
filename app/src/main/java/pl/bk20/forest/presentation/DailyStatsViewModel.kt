package pl.bk20.forest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.data.repository.DayRepositoryImpl
import pl.bk20.forest.data.repository.SettingsRepositoryImpl
import pl.bk20.forest.domain.usecase.DayUseCases
import pl.bk20.forest.domain.usecase.StatsUseCases
import java.time.LocalDate
import kotlin.math.roundToInt

class DailyStatsViewModel(
    private val dayUseCases: DayUseCases,
    private val statsUseCases: StatsUseCases
) : ViewModel() {

    private val _day = MutableStateFlow(
        DailyStatsState(
            date = LocalDate.MIN,
            stepsTaken = 0,
            treeCollected = false,
            calorieBurned = 0,
            distanceTravelled = 0f,
            carbonDioxideSaved = 0f,
        )
    )

    val day: StateFlow<DailyStatsState> = _day.asStateFlow()

    private val _dateRange = MutableStateFlow(LocalDate.now()..LocalDate.now())
    val dateRange: StateFlow<ClosedRange<LocalDate>> = _dateRange.asStateFlow()

    private var selectDateJob: Job? = null

    init {
        selectDay(LocalDate.now())
        viewModelScope.launch {
            statsUseCases.getFirstDayDate().collect {
                val firstDate = it ?: LocalDate.now()
                _dateRange.value = firstDate..LocalDate.now()
            }
        }
    }

    fun selectDay(date: LocalDate) {
        selectDateJob?.cancel()
        selectDateJob = dayUseCases.getDay(date).onEach {
            _day.value = day.value.copy(
                date = it.date,
                stepsTaken = it.steps,
                treeCollected = it.steps >= it.goal,
                calorieBurned = it.calorieBurned.roundToInt(),
                distanceTravelled = it.distanceTravelled,
                carbonDioxideSaved = it.carbonDioxideSaved
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
            val statsUseCases = StatsUseCases(dayRepository)

            return DailyStatsViewModel(dayUseCases, statsUseCases) as T
        }
    }
}