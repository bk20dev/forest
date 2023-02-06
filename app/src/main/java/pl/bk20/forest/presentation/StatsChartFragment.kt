package pl.bk20.forest.presentation

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
import kotlinx.coroutines.launch
import pl.bk20.forest.databinding.FragmentStatsChartBinding
import pl.bk20.forest.domain.model.Day
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import com.google.android.material.R as RMaterial

class StatsChartFragment : Fragment() {

    companion object {
        const val ARG_FIRST_DAY = "__first_day"
    }

    private lateinit var binding: FragmentStatsChartBinding

    private val viewModel: StatsChartViewModel by viewModels { StatsChartViewModel.Factory }
    private val statsViewModel: StatsViewModel by activityViewModels { StatsViewModel }

    private val chartAdapter = ChartAdapter {
        statsViewModel.selectDay(it.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        val firstDay = arguments?.getSerializable(ARG_FIRST_DAY) as LocalDate?
        firstDay?.let { viewModel.selectWeek(firstDay) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.week.collect { week ->
                    val highestStepsValue = week.maxOfOrNull { it.steps } ?: 1
                    val locale = resources.configuration.locales[0]
                    val chartValues = week.toChartValues(highestStepsValue, locale)
                    chartAdapter.submitList(chartValues)
                }
            }
        }
    }

    private fun List<Day>.toChartValues(
        max: Int,
        locale: Locale
    ): List<ChartAdapter.ChartValue<LocalDate>> = map {
        val value = it.steps / max.toFloat()
        val weekdayName = it.date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        val color = if (it.steps >= it.goal) {
            RMaterial.attr.colorPrimary
        } else {
            RMaterial.attr.colorPrimaryContainer
        }
        ChartAdapter.ChartValue(it.date, value, weekdayName, color)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerViewChart.apply {
            adapter = chartAdapter
        }
    }
}