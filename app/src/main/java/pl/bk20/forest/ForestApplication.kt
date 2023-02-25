package pl.bk20.forest

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.android.material.color.DynamicColors
import pl.bk20.forest.core.data.source.ForestDatabase
import pl.bk20.forest.core.data.source.SettingsStore
import pl.bk20.forest.core.data.source.SettingsStoreImpl

class ForestApplication : Application() {

    lateinit var settingsStore: SettingsStore
    lateinit var forestDatabase: ForestDatabase

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        PreferenceManager.setDefaultValues(this, R.xml.settings, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        settingsStore = SettingsStoreImpl(sharedPreferences)

        forestDatabase = Room.databaseBuilder(
            applicationContext,
            ForestDatabase::class.java,
            ForestDatabase.DATABASE_NAME
        ).build()
    }
}