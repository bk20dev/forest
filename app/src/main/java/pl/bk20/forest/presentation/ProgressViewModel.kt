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
import pl.bk20.forest.domain.usecase.SettingsUseCases
import java.time.LocalDate

class ProgressViewModel(
    private val dayUseCases: DayUseCases,
    private val settingsUseCases: SettingsUseCases,
    initialDate: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _progress = MutableStateFlow(ProgressState(LocalDate.MIN, 0, 0))
    val progress: StateFlow<ProgressState> = _progress.asStateFlow()

    private var getProgressJob: Job? = null

    init {
        getProgress(initialDate)
    }

    private fun getProgress(date: LocalDate) {
        getProgressJob?.cancel()

        val settingsFlow = settingsUseCases.getSettings()
        val dayFlow = dayUseCases.getDay(date)

        getProgressJob = settingsFlow.combine(dayFlow) { settings, day ->
            day?.run {
                progress.value.copy(
                    date = date,
                    steps = steps,
                    goal = goal
                )
            } ?: ProgressState(
                date = date,
                steps = 0,
                goal = settings.dailyGoal
            )
        }.onEach {
            _progress.value = it
        }.launchIn(viewModelScope)
    }

    companion object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication

            val dayDatabase = application.forestDatabase
            val dayRepository = DayRepositoryImpl(dayDatabase.dayDao)
            val dayUseCases = DayUseCases(dayRepository)

            val settingsStore = application.settingsStore
            val settingsRepository = SettingsRepositoryImpl(settingsStore)
            val settingsUseCases = SettingsUseCases(settingsRepository)

            return ProgressViewModel(dayUseCases, settingsUseCases) as T
        }
    }
}