package pl.bk20.forest

import android.app.Application
import com.google.android.material.color.DynamicColors

class ForestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}