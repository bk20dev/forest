package pl.bk20.forest.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pl.bk20.forest.databinding.ActivityMainBinding
import pl.bk20.forest.domain.util.MidnightTimer
import pl.bk20.forest.domain.util.TimerImpl
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

        timer.start()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.steps.collect {
                    updateProgress(it.stepCount, it.dailyGoal)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.stop()
    }

    private fun updateProgress(stepCount: Int, dailyGoal: Int) {
        binding.progressIndicator.apply {
            progress = stepCount
            max = dailyGoal
        }
    }
}