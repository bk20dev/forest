package pl.bk20.forest.activity_tracker_service.data.source.converters

import androidx.room.TypeConverter
import java.time.LocalDate

@Suppress("unused")
class LocalDateConverter {

    @TypeConverter
    fun localDateToEpochDay(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun epochDayToLocalDate(epochDay: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDay)
    }
}

