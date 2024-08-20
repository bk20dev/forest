package pl.bk20.forest.activity_tracker_service.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class ActivityTrackerViewModel : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // TODO: Pass all required initializers to the view model
                // val application = checkNotNull(this[APPLICATION_KEY]) as ForestApplication
                ActivityTrackerViewModel()
            }
        }
    }
}