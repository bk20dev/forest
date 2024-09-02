package pl.bk20.forest.activity_tracker_service.data.source.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

@Suppress("unused")
class LocalDateTimeConverter {

    @TypeConverter
    fun localDateTimeToEpochDay(dateTime: LocalDateTime): Long {
        return dateTime.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun epochDayToLocalDateTime(epochSecondUtc: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(epochSecondUtc, 0, ZoneOffset.UTC)
    }
}

