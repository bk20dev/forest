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

class ProgressViewModel(
    private val dayUseCases: DayUseCases,
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

        getProgressJob = dayUseCases.getDay(date).onEach { day ->
            _progress.value = progress.value.copy(
                date = day.date,
                steps = day.steps,
                goal = day.goal
            )
        }.launchIn(viewModelScope)
    }

    companion object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication

            val settingsStore = application.settingsStore
            val settingsRepository = SettingsRepositoryImpl(settingsStore)
            val dayDatabase = application.forestDatabase
            val dayRepository = DayRepositoryImpl(dayDatabase.dayDao)
            val dayUseCases = DayUseCases(dayRepository, settingsRepository)

            return ProgressViewModel(dayUseCases) as T
        }
    }
}