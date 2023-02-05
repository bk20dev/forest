package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
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
        val adapter = StatsChartFragmentAdapter(this) {
            Toast.makeText(context, "Selected $it", Toast.LENGTH_SHORT).show()
            // TODO: Handle date selection event
        }
        binding.viewPagerChart.adapter = adapter
    }
}

class StatsChartFragmentAdapter(
    fragment: Fragment,
    private val listener: StatsChartFragment.OnDateSelectedListener
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val fragment = StatsChartFragment(listener)
        fragment.arguments = Bundle().apply {
            val daysToSubtract = 6 + position * 7
            val firstDay = LocalDate.now().minusDays(daysToSubtract.toLong())
            putSerializable(StatsChartFragment.ARG_FIRST_DAY, firstDay)
        }
        return fragment
    }
}