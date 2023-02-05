package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pl.bk20.forest.databinding.FragmentStatsChartBinding
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import com.google.android.material.R as MaterialR

class StatsChartFragment : Fragment() {

    companion object {
        const val ARG_FIRST_DAY = "__first_day"
    }

    private lateinit var binding: FragmentStatsChartBinding
    private val viewModel: StatsChartViewModel by viewModels { StatsChartViewModel.Factory }

    private val chartAdapter = ChartAdapter<LocalDate> {
        Toast.makeText(context, "${it.id} selected", Toast.LENGTH_SHORT).show()
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
                    val chartValues = week.map { day ->
                        val value = day.steps.toFloat() / highestStepsValue
                        val weekdayName = day.date.dayOfWeek
                            .getDisplayName(TextStyle.SHORT, locale)
                        val dailyGoalReached = day.steps >= day.goal
                        val color = if (dailyGoalReached) {
                            MaterialR.attr.colorPrimary
                        } else {
                            MaterialR.attr.colorPrimaryContainer
                        }
                        ChartAdapter.ChartValue(day.date, value, weekdayName, color)
                    }
                    chartAdapter.submitList(chartValues)
                }
            }
        }
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