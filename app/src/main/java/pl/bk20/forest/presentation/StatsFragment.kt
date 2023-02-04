package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val adapter = StatsChartFragmentAdapter(this)
        binding.viewPagerChart.adapter = adapter
    }
}

class StatsChartFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val fragment = StatsChartFragment()
        fragment.arguments = Bundle().apply {
            val firstDay = LocalDate.now().minusDays(6)
            putSerializable(StatsChartFragment.ARG_FIRST_DAY, firstDay)
        }
        return fragment
    }
}