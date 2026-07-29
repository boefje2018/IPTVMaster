package com.iptv.master.util

object Constants {
    const val TAG = "IPTVMaster"
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p/"
    const val TMDB_API_KEY = "8265bd1679663a7ea12ac168da84d2e8"
    const val GITHUB_RAW_BASE = "https://raw.githubusercontent.com/"
    const val DEFAULT_PLAYLIST_URL = "https://raw.githubusercontent.com/iptv-org/iptv/master/index.m3u"
    const val DEFAULT_EPG_URL = "https://raw.githubusercontent.com/iptv-org/epg/master/epg.xml"
    const val GITHUB_RELEASES_API = "https://api.github.com/repos/%s/%s/releases/latest"
    const val GITHUB_REPO_OWNER = "iptvmaster"
    const val GITHUB_REPO_NAME = "IPTVMaster"
    const val UPDATE_CHECK_INTERVAL_HOURS = 24L
    const val PLAYLIST_SYNC_INTERVAL_HOURS = 12L
    const val EPG_SYNC_INTERVAL_HOURS = 6L
    const val MAX_HISTORY_ITEMS = 50
    const val BUFFER_SIZE_DEFAULT = 5000
    const val PREFS_NAME = "iptv_master_prefs"
    const val KEY_THEME = "theme_mode"
    const val KEY_LANGUAGE = "language"
    const val KEY_DYNAMIC_COLOR = "dynamic_color"
    const val KEY_BUFFER_SIZE = "buffer_size"
    const val KEY_HW_DECODING = "hardware_decoding"
    const val KEY_BACKGROUND_PLAY = "background_play"
    const val KEY_PIP_MODE = "pip_mode"
    const val KEY_PARENTAL_PIN = "parental_pin"
    const val KEY_ADULT_LOCK = "adult_lock"
    const val KEY_DEFAULT_PLAYLIST_URL = "default_playlist_url"
    const val KEY_DEFAULT_EPG_URL = "default_epg_url"
    const val KEY_LAST_UPDATE_CHECK = "last_update_check"
    const val DONATION_CONFIG_ASSET = "donation_config.json"
}
