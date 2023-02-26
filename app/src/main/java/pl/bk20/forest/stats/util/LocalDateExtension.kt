package pl.bk20.forest.stats.util

import com.google.android.material.R
import pl.bk20.forest.core.domain.model.Day
import pl.bk20.forest.stats.presentation.ChartAdapter
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

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

fun List<Day>.toChartValues(
    max: Int,
    locale: Locale,
    activeDay: LocalDate
): List<ChartAdapter.ChartValue<LocalDate>> = map {
    val value = it.steps / max.toDouble()
    val weekdayName = it.date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
    val isSelected = it.date.isEqual(activeDay)
    val barColor =
        if (isSelected) R.attr.colorPrimary
        else R.attr.colorPrimaryContainer
    val textColor =
        if (isSelected) R.attr.colorPrimary
        else R.attr.colorAccent
    ChartAdapter.ChartValue(
        it.date,
        value = value,
        label = weekdayName,
        barColor = barColor,
        textColor = textColor
    )
}