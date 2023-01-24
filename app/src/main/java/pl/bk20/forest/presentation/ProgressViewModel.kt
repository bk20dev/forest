package pl.bk20.forest.presentation

import android.util.Log
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

class ProgressViewModel(
    private val stepsUseCases: StepsUseCases,
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
        getProgressJob = stepsUseCases.getSteps(date).onEach {
            _progress.value = progress.value.copy(
                steps = it?.count ?: 0
            )
            Log.d("ProgressViewModel", "Ayo got $it")
        }.launchIn(viewModelScope)
    }

    companion object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication
            val stepsDao = application.stepsDatabase.stepsDao
            val stepsRepository = StepsRepositoryImpl(stepsDao)
            val stepsUseCases = StepsUseCases(stepsRepository)
            return ProgressViewModel(stepsUseCases) as T
        }
    }
}