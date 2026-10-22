package com.me.recipe.shared.utils

import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {

    private val sdf = SimpleDateFormat("MMMMM d, yyyy")

    fun longToDate(long: Long): Date = Date(long)

    fun dateToLong(date: Date): Long {
        return date.time / 1000 // return seconds
    }

    // Ex: November 4, 2021
    fun dateToString(date: Date): String = sdf.format(date)

    fun stringToDate(string: String): Date = sdf.parse(string) ?: throw NullPointerException("Could not convert date string to Date object.")

    fun createTimestamp(): Date = Date()
}
