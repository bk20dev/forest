package pl.bk20.forest.stats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.core.data.repository.DayRepositoryImpl
import pl.bk20.forest.stats.domain.usecase.StatsChartUseCases
import pl.bk20.forest.stats.util.alignWeek
import java.time.LocalDate

class StatsChartViewModel(
    private val statsChartUseCases: StatsChartUseCases,
    currentDateFlow: StateFlow<LocalDate>
) : ViewModel() {

    private val _dataset = MutableStateFlow(StatsChartState.of(currentDateFlow.value))
    val dataset: StateFlow<StatsChartState> = _dataset.asStateFlow()

    init {
        val firstDateFlow = statsChartUseCases.getFirstDate()
        firstDateFlow
            .combine(currentDateFlow) { firstDate, currentDate -> firstDate..currentDate }
            .onEach { dateRange ->
                _dataset.value = dataset.value.copy(dateRange = dateRange)
            }
            .launchIn(viewModelScope)
    }

    private var getDatasetJob: Job? = null

    fun selectWeek(firstDay: LocalDate) {
        getDatasetJob?.cancel()
        getDatasetJob = viewModelScope.launch {
            statsChartUseCases.getWeek(firstDay).collect { week ->
                val alignedWeek = week.alignWeek(firstDay)
                _dataset.value = dataset.value.copy(week = alignedWeek)
            }
        }
    }

    companion object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication

            val forestDatabase = application.forestDatabase
            val dayRepository = DayRepositoryImpl(forestDatabase.dayDao)
            val statsChartUseCases = StatsChartUseCases(dayRepository)

            return StatsChartViewModel(statsChartUseCases, application.currentDate) as T
        }
    }
}