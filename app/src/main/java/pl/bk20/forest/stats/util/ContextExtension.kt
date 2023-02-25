package pl.bk20.forest.stats.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getThemeColor(
    @AttrRes attrColor: Int
): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrColor, typedValue, true)
    return typedValue.data
}