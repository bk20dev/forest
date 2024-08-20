package pl.bk20.forest.activity_tracker_service.service

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.time.Instant

class ActivityTrackerViewModel : ViewModel() {

    fun incrementStepCount(deltaStepCount: Int, timestamp: Instant) {
        Log.d(
            "ActivityTrackerViewModel",
            "Incrementing step count by $deltaStepCount at $timestamp"
        )
    }

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