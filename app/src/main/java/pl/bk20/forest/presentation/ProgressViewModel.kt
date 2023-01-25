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
import pl.bk20.forest.data.repository.SettingsRepositoryImpl
import pl.bk20.forest.data.repository.StepsRepositoryImpl
import pl.bk20.forest.domain.usecase.SettingsUseCases
import pl.bk20.forest.domain.usecase.StepsUseCases
import java.time.LocalDate

class ProgressViewModel(
    private val stepsUseCases: StepsUseCases,
    private val settingsUseCases: SettingsUseCases,
    initialDate: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _progress = MutableStateFlow(ProgressState(LocalDate.MIN, 0, 0))
    val progress: StateFlow<ProgressState> = _progress.asStateFlow()

    private var getProgressJob: Job? = null

    init {
        viewModelScope.launch {
            settingsUseCases.getSettings().collect {
                _progress.value = progress.value.copy(
                    goal = it.dailyGoal
                )
            }
        }
    }

    init {
        getProgress(initialDate)
    }

    private fun getProgress(date: LocalDate) {
        getProgressJob?.cancel()
        getProgressJob = stepsUseCases.getSteps(date).onEach {
            _progress.value = progress.value.copy(
                steps = it?.count ?: 0
            )
        }.launchIn(viewModelScope)
    }

    companion object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication

            val stepsDao = application.stepsDatabase.stepsDao
            val stepsRepository = StepsRepositoryImpl(stepsDao)
            val stepsUseCases = StepsUseCases(stepsRepository)

            val settingsStore = application.settingsStore
            val settingsRepository = SettingsRepositoryImpl(settingsStore)
            val settingsUseCases = SettingsUseCases(settingsRepository)

            return ProgressViewModel(stepsUseCases, settingsUseCases) as T
        }
    }
}