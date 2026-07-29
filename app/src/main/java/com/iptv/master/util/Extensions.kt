package com.iptv.master.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import com.iptv.master.domain.model.Channel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.openUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    } catch (_: Exception) {
        toast("Could not open URL")
    }
}

fun String.isValidUrl(): Boolean {
    return try {
        val uri = Uri.parse(this)
        uri.scheme != null && (uri.scheme == "http" || uri.scheme == "https" || uri.scheme == "rtmp" || uri.scheme == "rtsp")
    } catch (_: Exception) {
        false
    }
}

fun String.isValidMac(): Boolean {
    return this.matches(Regex("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"))
}

fun Long.toFormattedDate(pattern: String = "dd MMM yyyy"): String {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.format(Date(this))
    } catch (_: Exception) {
        ""
    }
}

fun Long.toFormattedTime(pattern: String = "HH:mm"): String {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.format(Date(this))
    } catch (_: Exception) {
        ""
    }
}

fun List<Channel>.filterByCategory(category: String): List<Channel> {
    return this.filter { it.category.equals(category, ignoreCase = true) || it.groupTitle.equals(category, ignoreCase = true) }
}

fun Modifier.noRipple(): Modifier = composed {
    this.drawWithContent {
        drawContent()
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        ?: return false
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
