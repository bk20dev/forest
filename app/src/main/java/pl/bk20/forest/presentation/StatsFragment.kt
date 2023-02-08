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
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.launch
import pl.bk20.forest.R
import pl.bk20.forest.databinding.FragmentStatsBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding

    private val viewModel: StatsViewModel by activityViewModels { StatsViewModel }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonPreviousDay.setOnClickListener {
            val selectedDay = viewModel.day.value.date
            viewModel.selectDay(selectedDay.minusDays(1))
        }
        binding.buttonNextDay.setOnClickListener {
            val selectedDay = viewModel.day.value.date
            viewModel.selectDay(selectedDay.plusDays(1))
        }
        val adapter = StatsChartFragmentAdapter(this)
        binding.viewPagerChart.adapter = adapter
        val locale = resources.configuration.locales[0]
        val dateFormatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(locale)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.day.collect {
                    binding.textSelectedDate.text = it.date.format(dateFormatter)
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
                    binding.viewGroupTree.isVisible = it.treeCollected
                    binding.textCalorieBurned.text = calorieText
                    binding.textDistanceTravelled.text = distanceText
                }
            }
        }
    }
}

class StatsChartFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val fragment = StatsChartFragment()
        fragment.arguments = Bundle().apply {
            val daysToSubtract = 6 + position * 7
            val firstDay = LocalDate.now().minusDays(daysToSubtract.toLong())
            putSerializable(StatsChartFragment.ARG_FIRST_DAY, firstDay)
        }
        return fragment
    }
}