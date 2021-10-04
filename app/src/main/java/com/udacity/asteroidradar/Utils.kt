package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun convertDateStringToFormattedString(date: Date, format: String, locale: Locale = Locale.getDefault()) : String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(date)
        }

        fun addDaysToDate(date: Date, daysToAdd: Int) : Date {
            val c = Calendar.getInstance()
            c.time = date
            c.add(Calendar.DATE, daysToAdd)
            return c.time
        }
    }

}