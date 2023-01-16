package pl.bk20.forest

import android.app.Application
import androidx.room.Room
import com.google.android.material.color.DynamicColors
import pl.bk20.forest.data.source.StepsDatabase

class ForestApplication : Application() {

    lateinit var stepsDatabase: StepsDatabase

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        stepsDatabase = Room.databaseBuilder(
            applicationContext,
            StepsDatabase::class.java,
            StepsDatabase.DATABASE_NAME
        ).build()
    }
}