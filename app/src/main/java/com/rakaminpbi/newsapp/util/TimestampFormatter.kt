package com.rakaminpbi.newsapp.util

import java.text.SimpleDateFormat
import java.util.*

class TimestampFormatter(
    private val pattern: String
) {
    private val dateFormatWithMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val dateFormatWithoutMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    fun formatTimestamp(timestamp: String): String {
        try {
            val inputFormat = if (timestamp.contains(".")) dateFormatWithMillis else dateFormatWithoutMillis
            val parsedDate = inputFormat.parse(timestamp)
            return outputDateFormat.format(parsedDate)
        } catch (e: Exception) {
            return "Unknown date"
        }
    }
}