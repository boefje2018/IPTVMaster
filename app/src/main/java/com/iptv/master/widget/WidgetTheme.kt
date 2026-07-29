package com.iptv.master.widget

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.iptv.master.R
import com.iptv.master.util.Constants

class WidgetTheme(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    private val isDarkTheme: Boolean
        get() = prefs.getString(Constants.KEY_THEME, "system") == "dark"

    fun getBackgroundColor(): Int {
        return if (isDarkTheme) {
            ContextCompat.getColor(context, R.color.widget_dark_background)
        } else {
            ContextCompat.getColor(context, R.color.widget_light_background)
        }
    }

    fun getTextColor(): Int {
        return if (isDarkTheme) {
            ContextCompat.getColor(context, R.color.widget_dark_text)
        } else {
            ContextCompat.getColor(context, R.color.widget_light_text)
        }
    }

    fun getSecondaryTextColor(): Int {
        return if (isDarkTheme) {
            ContextCompat.getColor(context, R.color.widget_dark_secondary_text)
        } else {
            ContextCompat.getColor(context, R.color.widget_light_secondary_text)
        }
    }

    fun getItemBackgroundColor(): Int {
        return if (isDarkTheme) {
            ContextCompat.getColor(context, R.color.widget_dark_item_background)
        } else {
            ContextCompat.getColor(context, R.color.widget_light_item_background)
        }
    }

    companion object {
        const val PREFS_NAME = "widget_prefs"
    }
}
