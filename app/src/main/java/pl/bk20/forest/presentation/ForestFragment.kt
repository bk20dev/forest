package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pl.bk20.forest.databinding.FragmentForestBinding

class ForestFragment : Fragment() {

    private val viewModel: ForestViewModel by viewModels { ForestViewModel.Factory }

    private lateinit var binding: FragmentForestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.trees.collect { updateUserInterface(it) }
            }
        }
    }

    private fun updateUserInterface(forestState: ForestState) {
        binding.textTreesCollected.text = forestState.treeCount.toString()
    }
}