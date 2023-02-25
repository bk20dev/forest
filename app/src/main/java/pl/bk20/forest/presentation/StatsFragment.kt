package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import pl.bk20.forest.R
import pl.bk20.forest.databinding.FragmentStatsBinding

class StatsPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> DailyStatsFragment()
        else -> StatsSummaryFragment()
    }
}

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
        val statsPageAdapter = StatsPageAdapter(this)
        binding.pager.apply {
            isUserInputEnabled = false
            adapter = statsPageAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            val tabTitleRes = when (position) {
                0 -> R.string.details
                else -> R.string.summary
            }
            tab.text = getString(tabTitleRes)
        }.attach()
    }
}