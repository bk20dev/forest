package pl.bk20.forest.activity_tracker_service.data.entity

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import pl.bk20.forest.activity_tracker_service.data.source.converters.InstantConverter
import pl.bk20.forest.activity_tracker_service.data.source.converters.LocalDateTimeConverter
import java.time.Instant
import java.time.LocalDateTime

@TypeConverters(
    InstantConverter::class, LocalDateTimeConverter::class
)
data class TimeBucket(
    @ColumnInfo(name = "start_timestamp")
    val startTimestamp: Instant,

    @ColumnInfo(name = "end_timestamp")
    val endTimestamp: Instant,

    @ColumnInfo(name = "start_local_date_time")
    val startLocalDateTime: LocalDateTime,
)

fun TimeBucket.beginsWith(other: TimeBucket): Boolean =
    startTimestamp == other.startTimestamp && startLocalDateTime == other.startLocalDateTime

fun TimeBucket.extendedWith(timestamp: Instant, localDateTime: LocalDateTime): TimeBucket {
    return copy(
        startTimestamp = minOf(startTimestamp, timestamp),
        endTimestamp = maxOf(endTimestamp, timestamp),
        startLocalDateTime = minOf(startLocalDateTime, localDateTime)
    )
}