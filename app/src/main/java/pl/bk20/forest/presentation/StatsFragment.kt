package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.launch
import pl.bk20.forest.R
import pl.bk20.forest.databinding.FragmentStatsBinding
import java.time.LocalDate

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding

    private val viewModel: StatsViewModel by viewModels { StatsViewModel }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = StatsChartFragmentAdapter(this) { date ->
            viewModel.selectDay(date)
        }
        binding.viewPagerChart.adapter = adapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.day.collect {
                    val stepsText = resources.getQuantityString(
                        R.plurals.step_count_format, it.stepsTaken, it.stepsTaken
                    )
                    val calorieText = getString(
                        R.string.calorie_burned_format, it.calorieBurned
                    )
                    val distanceText = getString(
                        R.string.distance_travelled_format, it.distanceTravelled
                    )
                    binding.textStepCount.text = stepsText
                    binding.textCalorieBurned.text = calorieText
                    binding.textDistanceTravelled.text = distanceText
                }
            }
        }
    }
}

class StatsChartFragmentAdapter(
    fragment: Fragment,
    private val listener: StatsChartFragment.OnDateSelectedListener
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val fragment = StatsChartFragment(listener)
        fragment.arguments = Bundle().apply {
            val daysToSubtract = 6 + position * 7
            val firstDay = LocalDate.now().minusDays(daysToSubtract.toLong())
            putSerializable(StatsChartFragment.ARG_FIRST_DAY, firstDay)
        }
        return fragment
    }
}