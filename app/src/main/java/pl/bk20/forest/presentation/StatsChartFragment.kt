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
import pl.bk20.forest.databinding.FragmentStatsChartBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class StatsChartFragment : Fragment() {

    private val statsViewModel: StatsViewModel by activityViewModels { StatsViewModel.Factory }

    private lateinit var binding: FragmentStatsChartBinding
    private val shortDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPreviousDay.setOnClickListener { changeSelectedDate(offset = -1) }
        binding.buttonNextDay.setOnClickListener { changeSelectedDate(offset = 1) }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.day.collect { updateUserInterface(it.date) }
            }
        }
    }

    private fun changeSelectedDate(offset: Long) {
        val currentDate = statsViewModel.day.value.date
        statsViewModel.selectDay(currentDate.plusDays(offset))
    }

    private fun updateUserInterface(date: LocalDate) {
        binding.apply {
            textSelectedDate.text = date.format(shortDateFormatter)
            buttonNextDay.isVisible = date.isBefore(LocalDate.now())
            buttonPreviousDay.isVisible = date.isAfter(LocalDate.MIN)
        }
    }
}