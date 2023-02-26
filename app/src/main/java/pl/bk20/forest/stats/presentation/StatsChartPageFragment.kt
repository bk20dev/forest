package pl.bk20.forest.stats.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pl.bk20.forest.core.domain.model.Day
import pl.bk20.forest.databinding.FragmentStatsPageChartBinding
import pl.bk20.forest.stats.util.toChartValues
import java.lang.Integer.max
import java.time.LocalDate

class StatsChartPageFragment : Fragment() {

    companion object {
        const val ARG_PAGE_NUMBER = "__page_number"
    }

    private lateinit var binding: FragmentStatsPageChartBinding

    private val statsChartPageViewModel: StatsChartPageViewModel by viewModels { StatsChartPageViewModel.Factory }
    private val statsDetailsViewModel: StatsDetailsViewModel by activityViewModels { StatsDetailsViewModel.Factory }

    private var pageNumber: Long = 0

    private val chartAdapter = ChartAdapter {
        statsDetailsViewModel.selectDay(it.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = arguments?.getLong(ARG_PAGE_NUMBER) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsPageChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerViewChart.apply {
            adapter = chartAdapter
        }

        lifecycleScope.launch {
            val activeDayFlow = statsDetailsViewModel.day
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    val weekFlow = statsChartPageViewModel.week
                    weekFlow.combine(activeDayFlow) { week, activeDay ->
                        updateUserInterface(week, activeDay.date)
                    }.collect()
                }
                launch {
                    activeDayFlow.collect {
                        updateSelectedWeek(it.chartDateRange.endInclusive)
                    }
                }
            }
        }
    }

    private fun updateUserInterface(week: List<Day>, activeDate: LocalDate) {
        val highestChartValue = week.maxOfOrNull { max(it.steps, it.goal) } ?: 1
        val locale = resources.configuration.locales[0]
        val chartValues = week.toChartValues(highestChartValue, locale, activeDate)
        chartAdapter.submitList(chartValues)
    }

    private fun updateSelectedWeek(lastDate: LocalDate) {
        val daysToSubtract = 7 * pageNumber + 6
        val firstDate = lastDate.minusDays(daysToSubtract)
        statsChartPageViewModel.selectWeek(firstDate)
    }
}