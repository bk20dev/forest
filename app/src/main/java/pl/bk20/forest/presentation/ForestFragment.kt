package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.bk20.forest.databinding.FragmentForestBinding

class ForestFragment : Fragment() {

    private lateinit var binding: FragmentForestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForestBinding.inflate(inflater, container, false)
        return binding.root
    }
}