package pl.bk20.forest.progress

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

    private val viewModel: ProgressViewModel by activityViewModels { ProgressViewModel }

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
                viewModel.progress.collect { progress -> updateUserInterface(progress) }
            }
        }
    }

    private fun updateUserInterface(state: ProgressState) {
        updateProgress(state)
        updateTree(state)
        updateTiles(state)
    }

    private fun updateProgress(state: ProgressState) = state.apply {
        val numberFormat = DecimalFormat.getIntegerInstance()
        val formattedStepCount = numberFormat.format(stepsTaken)
        val dailyGoalStepCount = numberFormat.format(dailyGoal)
        val dailyGoalText = getString(R.string.step_goal, dailyGoalStepCount)
        binding.apply {
            textStepCount.text = formattedStepCount
            textDailyGoal.text = dailyGoalText
            progressDailyGoal.max = dailyGoal
            progressDailyGoal.progress = stepsTaken
        }
    }

    private fun updateTree(state: ProgressState) = state.apply {
        val treeResource = getTreeResource(stepsTaken.toDouble() / dailyGoal)
        binding.imageTree.setImageResource(treeResource)
    }

    private fun updateTiles(state: ProgressState) = state.apply {
        val calorieText = getString(
            R.string.calorie_burned_format, calorieBurned
        )
        val distanceText = getString(
            R.string.distance_travelled_format, distanceTravelled
        )
        val carbonDioxideText = getString(
            R.string.carbon_dioxide_saved_format, carbonDioxideSaved
        )
        binding.apply {
            textCalorieBurned.text = calorieText
            textDistanceTravelled.text = distanceText
            textCarbonDioxideSaved.text = carbonDioxideText
        }
    }

    private fun getTreeResource(progress: Double) =
        when {
            progress < .2 -> R.drawable.stage_1
            progress < .4 -> R.drawable.stage_2
            progress < .6 -> R.drawable.stage_3
            progress < .8 -> R.drawable.stage_4
            progress < 1 -> R.drawable.stage_5
            else -> R.drawable.stage_6
        }
}