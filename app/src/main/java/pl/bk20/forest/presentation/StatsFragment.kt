package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pl.bk20.forest.databinding.FragmentStatsBinding
import java.time.LocalDate

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val chartAdapter = ChartAdapter<LocalDate> {
            Toast.makeText(context, "${it.id} selected", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewChart.apply {
            adapter = chartAdapter
        }
    }
}