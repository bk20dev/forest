package pl.bk20.forest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import pl.bk20.forest.databinding.ActivityMainBinding
import pl.bk20.forest.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationController: NavController

    companion object {
        val topLevelRoutes = setOf(
            R.id.fragment_forest,
            R.id.fragment_progress,
            R.id.fragment_stats
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationController = getNavigationController()
        setupNavigationViewsWithNavigationController(navigationController)
    }

    private fun getNavigationController(): NavController {
        val navigationHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_navigation_host_fragment) as NavHostFragment
        return navigationHostFragment.navController
    }

    private fun setupNavigationViewsWithNavigationController(navigationController: NavController) {
        val appBarConfiguration = AppBarConfiguration(topLevelRoutes)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navigationController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navigationController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_settings -> {
            openSettings()
            true
        }

        else -> false
    }

    private fun openSettings() {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
    }
}