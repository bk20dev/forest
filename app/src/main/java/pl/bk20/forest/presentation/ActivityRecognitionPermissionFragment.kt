package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.bk20.forest.databinding.FragmentActivityRecognitionPermissionBinding

class ActivityRecognitionPermissionFragment : Fragment() {

    private var _binding: FragmentActivityRecognitionPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityRecognitionPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }
}