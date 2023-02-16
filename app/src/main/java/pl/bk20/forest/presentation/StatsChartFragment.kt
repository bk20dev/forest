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
import pl.bk20.forest.databinding.FragmentStatsChartBinding
import pl.bk20.forest.util.firstDayOfWeek
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.max

class ChartPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var pageCount = 0
    private var lastDate = LocalDate.now()

    fun getPageContaining(selectedDate: LocalDate, dateRange: ClosedRange<LocalDate>): Int {
        val upcomingWeek = dateRange.endInclusive.plusWeeks(1)
        val lastDayOfWeek = upcomingWeek.firstDayOfWeek.minusDays(1)
        val period = Period.between(selectedDate, lastDayOfWeek)
        return max(0, period.days / 7)
    }

    fun updatePageCount(range: ClosedRange<LocalDate>) {
        pageCount = getPageContaining(range.start, range) + 1
        lastDate = range.endInclusive
    }

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        val fragment = StatsChartPageFragment()
        fragment.arguments = Bundle().apply {
            val date = lastDate.minusDays(position * 7L)
            putSerializable(StatsChartPageFragment.ARG_FIRST_DAY, date.firstDayOfWeek)
        }
        return fragment
    }
}

class StatsChartFragment : Fragment() {

    private val statsViewModel: StatsViewModel by activityViewModels { StatsViewModel.Factory }

    private lateinit var binding: FragmentStatsChartBinding
    private lateinit var chartPageAdapter: ChartPageAdapter

    private val shortDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

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
                with(statsViewModel) {
                    launch { day.collect { updateUserInterface(it.date, dateRange.value) } }
                    launch { dateRange.collect { updateUserInterface(day.value.date, it) } }
                }
            }
        }
    }

    private fun changeSelectedDate(offset: Long) {
        val currentDate = statsViewModel.day.value.date
        statsViewModel.selectDay(currentDate.plusDays(offset))
    }

    private fun updateUserInterface(date: LocalDate, dateRange: ClosedRange<LocalDate>) {
        binding.apply {
            textSelectedDate.text = date.format(shortDateFormatter)
            val lastDate = dateRange.endInclusive.firstDayOfWeek.plusWeeks(1).minusDays(1)
            val firstDate = dateRange.start.firstDayOfWeek
            buttonNextDay.isVisible = date.isBefore(lastDate)
            buttonPreviousDay.isVisible = date.isAfter(firstDate)
            updateChartRange(dateRange)
            scrollChartTo(date, dateRange)
        }
    }

    private fun updateChartRange(range: ClosedRange<LocalDate>) {
        chartPageAdapter.updatePageCount(range)
    }

    private fun scrollChartTo(
        selectedDate: LocalDate,
        range: ClosedRange<LocalDate>
    ) {
        val pageIndex = chartPageAdapter.getPageContaining(selectedDate, range)
        binding.viewPagerChart.currentItem = pageIndex
    }
}