package pl.bk20.forest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.data.repository.StepsRepositoryImpl
import pl.bk20.forest.domain.usecase.StepsUseCases
import java.time.LocalDate

class MainViewModel(
    private val stepsUseCases: StepsUseCases
) : ViewModel() {

    private val _steps = MutableStateFlow(MainState(LocalDate.now(), 0, 10000, 0f, 0))
    val steps: StateFlow<MainState> = _steps.asStateFlow()

    private var getStepsJob: Job? = null

    init {
        updateActiveDate(_steps.value.date)
    }

    fun updateActiveDate(date: LocalDate) {
        getSteps(date)
    }

    private fun getSteps(date: LocalDate) {
        getStepsJob?.cancel()
        getStepsJob = stepsUseCases.getSteps(date).onEach {
            val newStepCount = it?.count ?: 0
            _steps.value = steps.value.copy(
                date = date,
                takenSteps = newStepCount,
                distanceTravelledInKm = newStepCount * 7f / 10000, // 7 dm per step
                calorieBurned = newStepCount / 25 // 0.04 (1/25) kcal per step
            )
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