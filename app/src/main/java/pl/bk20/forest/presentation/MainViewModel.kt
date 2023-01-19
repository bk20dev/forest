package pl.bk20.forest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.data.repository.StepsRepositoryImpl
import pl.bk20.forest.domain.usecase.StepsUseCases
import java.time.LocalDate

class MainViewModel(
    private val stepsUseCases: StepsUseCases
) : ViewModel() {

    private val _steps = MutableStateFlow(MainState(0, 10000))
    val steps: StateFlow<MainState> = _steps.asStateFlow()

    private var getStepsJob: Job? = null

    init {
        // TODO: Change the day on midnight
        getSteps(LocalDate.now())
    }

    private fun getSteps(date: LocalDate) {
        getStepsJob?.cancel()
        getStepsJob = stepsUseCases.getSteps(date).onEach {
            val newStepCount = it?.count ?: 0
            _steps.value = steps.value.copy(stepCount = newStepCount)
        }.launchIn(viewModelScope)
    }

    companion object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication
            val stepsDao = application.stepsDatabase.stepsDao
            val stepsRepository = StepsRepositoryImpl(stepsDao)
            return MainViewModel(StepsUseCases(stepsRepository)) as T
        }
    }
}