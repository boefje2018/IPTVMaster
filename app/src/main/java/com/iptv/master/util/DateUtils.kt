package com.iptv.master.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    private const val XMLTV_FORMAT = "yyyyMMddHHmmss Z"
    private const val DISPLAY_DATE_FORMAT = "dd MMM yyyy"
    private const val DISPLAY_TIME_FORMAT = "HH:mm"
    private const val DISPLAY_DATETIME_FORMAT = "dd MMM yyyy HH:mm"

    fun parseXMLTVTime(xmltvTime: String): Long? {
        return try {
            val sdf = SimpleDateFormat(XMLTV_FORMAT, Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(xmltvTime)?.time
        } catch (_: Exception) {
            null
        }
    }

    fun formatDate(timestamp: Long, pattern: String = DISPLAY_DATE_FORMAT): String {
        return try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.format(Date(timestamp))
        } catch (_: Exception) {
            ""
        }
    }

    fun formatTime(timestamp: Long, pattern: String = DISPLAY_TIME_FORMAT): String {
        return try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.format(Date(timestamp))
        } catch (_: Exception) {
            ""
        }
    }

    fun formatDateTime(timestamp: Long): String {
        return formatDate(timestamp, DISPLAY_DATETIME_FORMAT)
    }

    fun formatDuration(startTime: Long, endTime: Long): String {
        val diff = endTime - startTime
        val hours = diff / 3600000
        val minutes = (diff % 3600000) / 60000
        return if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }

    fun getRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = timestamp - now
        return when {
            diff < 0 -> "Started ${formatDuration(now, timestamp)} ago"
            diff < 3600000 -> "In ${diff / 60000} min"
            diff < 86400000 -> "In ${diff / 3600000}h ${(diff % 3600000) / 60000}m"
            else -> formatDateTime(timestamp)
        }
    }

    fun isCurrentlyPlaying(start: Long, end: Long): Boolean {
        val now = System.currentTimeMillis()
        return now in start..end
    }

    fun isUpcoming(start: Long): Boolean {
        return start > System.currentTimeMillis()
    }
}
