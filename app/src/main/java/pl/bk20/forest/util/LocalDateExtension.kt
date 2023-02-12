package pl.bk20.forest.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.*

val LocalDate.firstDayOfWeek
    get(): LocalDate {
        val timeZone = TimeZone.getTimeZone(ZoneId.systemDefault())
        val firstDayOfWeek = Calendar.getInstance(timeZone).firstDayOfWeek
        val adjusters = TemporalAdjusters.previousOrSame(DayOfWeek.of(firstDayOfWeek))
        return with(adjusters)
    }

operator fun ClosedRange<LocalDate>.iterator() = object : Iterator<LocalDate> {

    private var current = start.minusDays(1)

    override fun hasNext(): Boolean {
        return current.isBefore(endInclusive)
    }

    override fun next(): LocalDate {
        if (current.isBefore(endInclusive)) {
            current = current.plusDays(1)
        }
        return current
    }
}