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
import pl.bk20.forest.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private val viewModel: StatsViewModel by activityViewModels { StatsViewModel.Factory }

    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.day.collect { updateUserInterface(it) }
            }
        }
    }

    private fun updateUserInterface(state: StatsState) {
        val stepsText = resources.getQuantityString(
            R.plurals.step_count_format, state.stepsTaken, state.stepsTaken
        )
        val calorieText = getString(
            R.string.calorie_burned_format, state.calorieBurned
        )
        val distanceText = getString(
            R.string.distance_travelled_format, state.distanceTravelled
        )
        binding.textStepCount.text = stepsText
        binding.viewGroupTree.isVisible = state.treeCollected
        binding.textCalorieBurned.text = calorieText
        binding.textDistanceTravelled.text = distanceText
    }
}
