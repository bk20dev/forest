package pl.bk20.forest.trees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.core.data.repository.DayRepositoryImpl
import pl.bk20.forest.trees.domain.usecase.ForestUseCases

class ForestViewModel(
    forestUseCases: ForestUseCases
) : ViewModel() {

    private val _trees = MutableStateFlow(ForestState(treeCount = 0))
    val trees: StateFlow<ForestState> = _trees.asStateFlow()

    init {
        viewModelScope.launch {
            forestUseCases.getTreeCount().collect {
                _trees.value = _trees.value.copy(
                    treeCount = it
                )
            }
        }
    }

    object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication
            val forestDatabase = application.forestDatabase
            val dayRepository = DayRepositoryImpl(forestDatabase.dayDao)
            val forestUseCases = ForestUseCases(dayRepository)
            return ForestViewModel(forestUseCases) as T
        }
    }
}