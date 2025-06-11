package com.optiroute.com.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

fun Long.toDateString(pattern: String = "dd MMM yyyy"): String {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.format(Date(this))
    } catch (e: Exception) {
        ""
    }
}

fun Double.toFormattedDistance(): String {
    return LocationUtils.formatDistance(this)
}

fun Long.toFormattedDuration(): String {
    return LocationUtils.formatDuration(this)
}

fun String.toCapitalCase(): String {
    return this.lowercase().split(" ").joinToString(" ") { word ->
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}

fun Double.roundTo(decimals: Int): Double {
    val multiplier = Math.pow(10.0, decimals.toDouble())
    return round(this * multiplier) / multiplier
}

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPhoneNumber(): Boolean {
    return android.util.Patterns.PHONE.matcher(this).matches()
}

fun List<*>.isNotNullOrEmpty(): Boolean {
    return this.isNotEmpty()
}

fun String.removeSpaces(): String {
    return this.replace(" ", "")
}

fun String.toSnakeCase(): String {
    return this.replace(" ", "_").uppercase()
}

fun String.fromSnakeCase(): String {
    return this.replace("_", " ").toCapitalCase()
}