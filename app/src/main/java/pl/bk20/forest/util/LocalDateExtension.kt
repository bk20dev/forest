package pl.bk20.forest.util

import java.time.LocalDate

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