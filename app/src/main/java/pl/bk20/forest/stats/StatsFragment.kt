package pl.bk20.forest.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import pl.bk20.forest.R
import pl.bk20.forest.databinding.FragmentStatsBinding
import pl.bk20.forest.stats.presentation.StatsDetailsFragment
import pl.bk20.forest.stats.presentation.StatsSummaryFragment

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding

    companion object {

        private val fragments = listOf(
            R.string.details to { StatsDetailsFragment() },
            R.string.summary to { StatsSummaryFragment() },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val statsPageAdapter = StatsPageAdapter(this)
        binding.pager.apply {
            isUserInputEnabled = false
            adapter = statsPageAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            val tabTitleRes = fragments[position].first
            tab.text = getString(tabTitleRes)
        }.attach()
    }

    class StatsPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position].second()
        }
    }
}