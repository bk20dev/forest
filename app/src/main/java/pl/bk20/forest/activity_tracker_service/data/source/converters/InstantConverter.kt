package pl.bk20.forest.activity_tracker_service.data.source.converters

import androidx.room.TypeConverter
import java.time.Instant

@Suppress("unused")
class InstantConverter {

    @TypeConverter
    fun instantToEpochMilli(instant: Instant): Long {
        return instant.toEpochMilli()
    }

    @TypeConverter
    fun epochMilliToInstant(epochMilli: Long): Instant {
        return Instant.ofEpochMilli(epochMilli)
    }
}