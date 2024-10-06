package pl.bk20.forest.activity_tracker_service.data.entity

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import pl.bk20.forest.activity_tracker_service.data.source.converters.InstantConverter
import pl.bk20.forest.activity_tracker_service.data.source.converters.LocalDateTimeConverter
import java.time.Instant
import java.time.LocalDateTime

@TypeConverters(
    InstantConverter::class,
    LocalDateTimeConverter::class
)
data class ActivityTimeRange(

    @ColumnInfo(name = "start_instant")
    val startInstant: Instant,

    @ColumnInfo(name = "start_local_date_time")
    val startLocalDateTime: LocalDateTime,

    @ColumnInfo(name = "end_instant")
    val endInstant: Instant,

    @ColumnInfo(name = "end_local_date_time")
    val endLocalDateTime: LocalDateTime,
)