package pl.bk20.forest.activity_tracker_service.data.source.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

@Suppress("unused")
class LocalDateTimeConverter {

    companion object {
        private val defaultZoneOffset = ZoneOffset.UTC
    }

    @TypeConverter
    fun localDateTimeToEpochSecond(localDateTime: LocalDateTime): Long {
        return localDateTime.toEpochSecond(defaultZoneOffset)
    }

    @TypeConverter
    fun epochSecondToLocalDateTime(epochSecondUtc: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(epochSecondUtc, 0, defaultZoneOffset)
    }
}

