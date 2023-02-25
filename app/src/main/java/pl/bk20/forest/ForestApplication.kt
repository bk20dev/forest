package pl.bk20.forest

import android.app.Application
import android.content.Intent
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.android.material.color.DynamicColors
import kotlinx.coroutines.flow.MutableStateFlow
import pl.bk20.forest.core.data.source.ForestDatabase
import pl.bk20.forest.settings.data.source.SettingsStore
import pl.bk20.forest.settings.data.source.SettingsStoreImpl
import java.time.LocalDate

class ForestApplication : Application() {

    lateinit var settingsStore: SettingsStore
    lateinit var forestDatabase: ForestDatabase

    val currentDate = MutableStateFlow<LocalDate>(LocalDate.now())

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

        val midnightBroadcastReceiverIntent =
            Intent(applicationContext, MidnightBroadcastReceiver::class.java)
        sendBroadcast(midnightBroadcastReceiverIntent)
    }
}