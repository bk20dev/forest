package pl.bk20.forest

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.android.material.color.DynamicColors
import pl.bk20.forest.data.source.SettingsStore
import pl.bk20.forest.data.source.SettingsStoreImpl
import pl.bk20.forest.data.source.StepsDatabase

class ForestApplication : Application() {

    lateinit var settingsStore: SettingsStore
    lateinit var stepsDatabase: StepsDatabase

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        PreferenceManager.setDefaultValues(this, R.xml.settings, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        settingsStore = SettingsStoreImpl(sharedPreferences)

        stepsDatabase = Room.databaseBuilder(
            applicationContext,
            StepsDatabase::class.java,
            StepsDatabase.DATABASE_NAME
        ).build()
    }
}