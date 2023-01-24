package pl.bk20.forest.presentation

import android.os.Bundle
import android.view.Menu
import androidx.preference.PreferenceFragmentCompat
import pl.bk20.forest.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

}