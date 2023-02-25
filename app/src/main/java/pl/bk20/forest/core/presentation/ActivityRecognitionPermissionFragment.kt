package pl.bk20.forest.core.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pl.bk20.forest.R
import pl.bk20.forest.databinding.FragmentActivityRecognitionPermissionBinding

@RequiresApi(Build.VERSION_CODES.Q)
class ActivityRecognitionPermissionFragment : Fragment() {

    private var _binding: FragmentActivityRecognitionPermissionBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) {
        when (ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACTIVITY_RECOGNITION
        )) {
            PackageManager.PERMISSION_GRANTED -> openMainActivity()
            PackageManager.PERMISSION_DENIED -> openPermissionSettings()
        }
    }

    private fun openMainActivity() {
        val action = R.id.action_activityRecognitionPermissionFragment_to_mainActivity
        findNavController().navigate(action)
        requireActivity().finish()
    }

    private fun openPermissionSettings() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", requireContext().packageName, null)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityRecognitionPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonContinue.setOnClickListener {
            requestPermission()
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
    }
}