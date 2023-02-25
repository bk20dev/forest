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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pl.bk20.forest.databinding.FragmentStatsPageChartBinding
import pl.bk20.forest.domain.model.Day
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import com.google.android.material.R as RMaterial

class StatsChartPageFragment : Fragment() {

    companion object {
        const val ARG_FIRST_DAY = "__first_day"
    }

    private lateinit var binding: FragmentStatsPageChartBinding

    private val viewModel: StatsChartViewModel by viewModels { StatsChartViewModel.Factory }
    private val dailyStatsViewModel: DailyStatsViewModel by activityViewModels { DailyStatsViewModel }

    private val chartAdapter = ChartAdapter {
        dailyStatsViewModel.selectDay(it.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        val firstDay = arguments?.getSerializable(ARG_FIRST_DAY) as LocalDate?
        firstDay?.let { viewModel.selectWeek(firstDay) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                val weekFlow = viewModel.week
                val dayFlow = dailyStatsViewModel.day

                weekFlow.combine(dayFlow) { week, day ->
                    val highestStepsValue = week.maxOfOrNull { it.steps } ?: 1
                    val locale = resources.configuration.locales[0]
                    week.toChartValues(highestStepsValue, locale, day.date)
                }.collect {
                    chartAdapter.submitList(it)
                }
            }
        }
    }

    private fun List<Day>.toChartValues(
        max: Int,
        locale: Locale,
        activeDay: LocalDate
    ): List<ChartAdapter.ChartValue<LocalDate>> = map {
        val value = it.steps / max.toDouble()
        val weekdayName = it.date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        val isSelected = it.date.isEqual(activeDay)
        val barColor =
            if (isSelected) RMaterial.attr.colorPrimary
            else RMaterial.attr.colorPrimaryContainer
        val textColor =
            if (isSelected) RMaterial.attr.colorPrimary
            else RMaterial.attr.colorAccent
        ChartAdapter.ChartValue(
            it.date,
            value = value,
            label = weekdayName,
            barColor = barColor,
            textColor = textColor
        )
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
    }
}