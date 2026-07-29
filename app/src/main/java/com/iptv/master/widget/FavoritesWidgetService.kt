package com.iptv.master.widget

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.iptv.master.R
import com.iptv.master.domain.model.Channel
import com.iptv.master.util.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return FavoritesRemoteViewsFactory(applicationContext)
    }
}

class FavoritesRemoteViewsFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {

    private val channels = mutableListOf<Channel>()
    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun onCreate() {
        loadFavorites()
    }

    override fun onDataSetChanged() {
        loadFavorites()
    }

    private fun loadFavorites() {
        channels.clear()
        val json = prefs.getString("widget_favorites", "[]") ?: "[]"
        try {
            val type = object : TypeToken<List<Channel>>() {}.type
            val saved: List<Channel> = gson.fromJson(json, type)
            channels.addAll(saved)
        } catch (_: Exception) { }
    }

    override fun getCount(): Int = channels.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position >= channels.size) {
            return RemoteViews(context.packageName, R.layout.widget_item)
        }

        val channel = channels[position]
        val views = RemoteViews(context.packageName, R.layout.widget_item)

        views.setTextViewText(R.id.channel_name, channel.name)
        views.setTextViewText(R.id.channel_category, channel.category)

        val fillInIntent = Intent().apply {
            putExtra("channelId", channel.id)
        }
        views.setOnClickFillInIntent(R.id.widget_item_root, fillInIntent)

        val themeManager = WidgetTheme(context)
        views.setTextColor(R.id.channel_name, themeManager.getTextColor())
        views.setTextColor(R.id.channel_category, themeManager.getSecondaryTextColor())

        if (!channel.logoUrl.isNullOrBlank()) {
            try {
                val future = Glide.with(context)
                    .asBitmap()
                    .load(channel.logoUrl)
                    .submit(48, 48)
                val bitmap = future.get()
                views.setImageViewBitmap(R.id.channel_logo, bitmap)
            } catch (_: Exception) {
                views.setImageViewResource(R.id.channel_logo, R.drawable.ic_tv)
            }
        } else {
            views.setImageViewResource(R.id.channel_logo, R.drawable.ic_tv)
        }

        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
    override fun onDestroy() { channels.clear() }
}
