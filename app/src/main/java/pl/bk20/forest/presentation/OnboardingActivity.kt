package pl.bk20.forest.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.bk20.forest.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}