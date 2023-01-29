package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pl.bk20.forest.R
import pl.bk20.forest.databinding.FragmentProgressBinding
import java.text.DecimalFormat

class ProgressFragment : Fragment() {

    private val viewModel: ProgressViewModel by activityViewModels { ProgressViewModel.Factory }

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.progress.collect { progress ->
                    updateProgress(progress)
                }
            }
        }
    }

    private fun updateProgress(progress: ProgressState) {
        val numberFormat = DecimalFormat.getIntegerInstance()
        binding.apply {
            textStepCount.text = numberFormat.format(progress.steps)
            val dailyGoalStepCount = numberFormat.format(progress.goal)
            textDailyGoal.text = getString(R.string.step_goal, dailyGoalStepCount)
            progressDailyGoal.max = progress.goal
            progressDailyGoal.progress = progress.steps
        }
    }
}