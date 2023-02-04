package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pl.bk20.forest.databinding.FragmentStatsChartBinding
import java.time.LocalDate

class StatsChartFragment : Fragment() {

    companion object {
        const val ARG_FIRST_DAY = "__first_day"
    }

    private lateinit var binding: FragmentStatsChartBinding

    private val chartAdapter = ChartAdapter<LocalDate> {
        Toast.makeText(context, "${it.id} selected", Toast.LENGTH_SHORT).show()
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
        chartAdapter.submitList(
            listOf(
                ChartAdapter.ChartValue(
                    LocalDate.now(),
                    0.6f,
                    "Sat",
                    com.google.android.material.R.attr.colorPrimaryContainer
                )
            )
        )
    }
}