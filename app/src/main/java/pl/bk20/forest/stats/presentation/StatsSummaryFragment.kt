package pl.bk20.forest.stats.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pl.bk20.forest.R
import pl.bk20.forest.databinding.FragmentStatsSummaryBinding
import kotlin.math.roundToInt


class StatsSummaryFragment : Fragment() {

    private lateinit var binding: FragmentStatsSummaryBinding

    private val viewModel: StatsSummaryViewModel by viewModels { StatsSummaryViewModel }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.swipeRefreshContainer.setOnRefreshListener {
            viewModel.refreshStatsSummary()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.statsSummary.collect { updateUserInterface(it) }
            }
        }
    }

    private fun updateUserInterface(state: StatsSummaryState) = state.apply {
        val treesText = resources.getQuantityString(
            R.plurals.trees_collected_format, treesCollected, treesCollected
        )
        val stepsText = resources.getQuantityString(
            R.plurals.step_count_format, stepsTaken.toInt(), stepsTaken
        )
        val calorieText = getString(
            R.string.calorie_burned_format, calorieBurned.roundToInt()
        )
        val distanceText = getString(
            R.string.distance_travelled_format, distanceTravelled
        )
        val carbonDioxideText = getString(
            R.string.carbon_dioxide_saved_format, carbonDioxideSaved
        )
        binding.apply {
            swipeRefreshContainer.isRefreshing = state.isRefreshing
            textTreesCollected.text = treesText
            textStepCount.text = stepsText
            textCalorieBurned.text = calorieText
            textDistanceTravelled.text = distanceText
            textCarbonDioxideSaved.text = carbonDioxideText
        }
    }
}