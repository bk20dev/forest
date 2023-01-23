package pl.bk20.forest.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pl.bk20.forest.R
import pl.bk20.forest.databinding.ActivityMainBinding
import pl.bk20.forest.domain.util.MidnightTimer
import pl.bk20.forest.domain.util.TimerImpl
import pl.bk20.forest.service.StepCounterService
import java.text.DecimalFormat
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModel }

    private val timer = MidnightTimer(TimerImpl()) {
        val today = LocalDate.now()
        viewModel.updateActiveDate(today)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resizeProgressCircle()

        timer.start()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.steps.collect { state ->
                    updateProgress(state)
                }
            }
        }

        startStepCounterService()
    }

    private fun updateProgress(state: MainState) {
        val numberFormat = DecimalFormat.getIntegerInstance()
        val stepsLeft = state.dailyGoal - state.takenSteps
        binding.apply {
            progressIndicator.progress = state.takenSteps
            progressIndicator.max = state.dailyGoal
            textStepCount.text = numberFormat.format(state.takenSteps)
            textStepsLeft.text = if (stepsLeft >= 0) {
                getString(R.string.steps_left, numberFormat.format(stepsLeft))
            } else {
                getString(R.string.steps_beyond_goal, numberFormat.format(-stepsLeft))
            }
            textCalorieBurned.text = state.calorieBurned.toString()
            @SuppressLint("SetTextI18n")
            textDistanceTravelled.text = "%.2f".format(state.distanceTravelledInKm)
        }
    }

    private fun resizeProgressCircle() {
        val parent = binding.progressIndicator.parent as View
        parent.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binding.progressIndicator.apply {
                    indicatorSize = parent.width
                    updateLayoutParams {
                        width = parent.width
                        height = parent.width
                    }
                }
            }
        })
    }

    private fun startStepCounterService() {
        val intent = Intent(this, StepCounterService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.stop()
    }
}