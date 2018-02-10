package com.circleVi.basekotlin.common.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    const val ISO_8601_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

    private val cacheDateFormat: HashMap<String, SimpleDateFormat> by lazy {
        HashMap<String, SimpleDateFormat>()
    }

    fun getDateFormat(pattern: String = ISO_8601_DATE_TIME_FORMAT): SimpleDateFormat {
        if (cacheDateFormat[pattern] == null) {
            val format = SimpleDateFormat(pattern, Locale.getDefault())
            format.isLenient = false
            cacheDateFormat[pattern] = format
        }

        return cacheDateFormat[pattern]!!
    }

    fun today(): Calendar {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        return today
    }

    /**
     * Formats a Calendar into a date/time string.
     *
     * @param date    the calendar instance
     * @param pattern the pattern describing the date and time format
     * @return the formatted time string
     */
    fun formatDateTime(date: Calendar, pattern: String): String {
        return try {
            val sdf = TimeUtils.getDateFormat(pattern)
            sdf.format(date.time)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun converToDate(input: String, format: String): Date? {
        return try {
            val sdf = TimeUtils.getDateFormat(format)
            sdf.parse(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getYearMonthDay(date: Date): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return Triple(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))
    }

    fun getDateFromYearMonthDay(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.time
    }
}

fun Date.getDayOfMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.getWeekOfMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
    return if (weekOfMonth == 5) -1 else weekOfMonth
}

/**
 * Check if `time` is in `x` days ago or not
 *
 * @param time The date need to check
 * @param x Number of days ago
 */
fun isInXDaysAgo(time: Date, x: Int): Boolean {
    val diff = System.currentTimeMillis() - time.time

    return diff <= 3_600_000 * 24 * x
}

/**
 * Checks if two times are on the same day.
 *
 * @param calendar The second day.
 * @return Whether the times are on the same day.
 */
fun Calendar.isTheSameDay(calendar: Calendar): Boolean {
    return this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
            && this.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
}

fun Calendar.isTheSameWeek(calendar: Calendar?): Boolean {
    if (calendar == null) {
        return false
    }

    return this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
            && this.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR)
}

