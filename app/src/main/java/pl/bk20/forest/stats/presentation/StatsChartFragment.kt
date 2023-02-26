package pl.bk20.forest.stats.presentation

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
import pl.bk20.forest.databinding.FragmentStatsChartBinding
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class StatsChartFragment : Fragment() {

    private val statsDetailsViewModel: StatsDetailsViewModel by activityViewModels { StatsDetailsViewModel.Factory }

    private lateinit var binding: FragmentStatsChartBinding
    private lateinit var chartPageAdapter: ChartPageAdapter

    private val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartPageAdapter = ChartPageAdapter(this)
        binding.viewPagerChart.adapter = chartPageAdapter

        binding.buttonPreviousDay.setOnClickListener { changeSelectedDate(offset = -1) }
        binding.buttonNextDay.setOnClickListener { changeSelectedDate(offset = 1) }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsDetailsViewModel.day.collect {
                    updateUserInterface(it.date, it.chartDateRange)
                }
            }
        }
    }

    private fun changeSelectedDate(offset: Long) {
        val currentDate = statsDetailsViewModel.day.value.date
        statsDetailsViewModel.selectDay(currentDate.plusDays(offset))
    }

    private fun updateUserInterface(selectedDate: LocalDate, dateRange: ClosedRange<LocalDate>) {
        binding.apply {
            textSelectedDate.text = selectedDate.format(dateFormatter)
            buttonPreviousDay.isVisible = selectedDate.isAfter(dateRange.start)
            buttonNextDay.isVisible = selectedDate.isBefore(dateRange.endInclusive)
            chartPageAdapter.dateRange = dateRange
            scrollChartTo(selectedDate)
        }
    }

    private fun scrollChartTo(
        selectedDate: LocalDate,
    ) {
        val pageIndex = chartPageAdapter.getPageContaining(selectedDate)
        binding.viewPagerChart.currentItem = pageIndex
    }

    class ChartPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        var dateRange = LocalDate.now()..LocalDate.now()

        fun getPageContaining(selectedDate: LocalDate): Int {
            val period = Period.between(selectedDate, dateRange.endInclusive)
            return (period.days / 7).coerceIn(0, itemCount)
        }

        override fun getItemCount(): Int = dateRange.run {
            val period = Period.between(start, endInclusive)
            return period.days / 7 + 1
        }

        override fun createFragment(position: Int): Fragment {
            val fragment = StatsChartPageFragment()
            fragment.arguments = Bundle().apply {
                putLong(StatsChartPageFragment.ARG_PAGE_NUMBER, position.toLong())
            }
            return fragment
        }
    }
}