package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pl.bk20.forest.R
import pl.bk20.forest.databinding.FragmentDailyStatsBinding

class DailyStatsFragment : Fragment() {

    private val viewModel: DailyStatsViewModel by activityViewModels { DailyStatsViewModel.Factory }

    private lateinit var binding: FragmentDailyStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.day.collect { updateUserInterface(it) }
            }
        }
    }

    private fun updateUserInterface(state: DailyStatsState) = state.apply {
        val stepsText = resources.getQuantityString(
            R.plurals.step_count_format, stepsTaken, stepsTaken
        )
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
            textStepCount.text = stepsText
            viewGroupTree.isVisible = treeCollected
            textCalorieBurned.text = calorieText
            textDistanceTravelled.text = distanceText
            textCarbonDioxideSaved.text = carbonDioxideText
        }
    }
}
