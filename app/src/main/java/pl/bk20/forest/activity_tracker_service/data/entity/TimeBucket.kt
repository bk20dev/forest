package pl.bk20.forest.activity_tracker_service.data.entity

import androidx.room.ColumnInfo
import java.time.Instant
import java.time.LocalDate

data class TimeBucket(
    @ColumnInfo(name = "start_timestamp")
    val startTimestamp: Instant,

    @ColumnInfo(name = "end_timestamp")
    val endTimestamp: Instant,

    @ColumnInfo(name = "start_local_date")
    val startLocalDate: LocalDate,
)

fun TimeBucket.extendedWith(timestamp: Instant, localDate: LocalDate): TimeBucket {
    return copy(
        startTimestamp = minOf(startTimestamp, timestamp),
        endTimestamp = maxOf(endTimestamp, timestamp),
        startLocalDate = minOf(startLocalDate, localDate)
    )
}