package com.iptv.master.util

import com.iptv.master.domain.model.ContentType

object ContentTypeDetector {

    private val AMBIGUOUS_TITLES = setOf(
        "general", "undefined", "entertainment", "lifestyle", "culture",
        "classic", "relax", "shop", "outdoor", "travel", "auto",
        "business", "legislative", "public"
    )

    private val LIVE_TITLE_KEYWORDS = setOf(
        "live", "canlı", "canli", "haber", "news", "sport", "spor",
        "canli tv", "canlı tv", "live tv", "tv live", "24/7",
        "kesintisiz", "kesintisiz yayın", "ultra hd", "fhd",
        "hd tv", "sd tv", "4k", "uhd", "radio", "radyo", "fm",
        "weather", "hava durumu", "music", "müzik", "kids", "çocuk",
        "comedy", "komedi", "documentary", "belgesel", "educational",
        "eğitim", "science", "bilim", "animation", "animasyon",
        "family", "aile", "cooking", "yemek", "travel", "gezi",
        "culture", "kültür", "relax", "dinlenme", "shop", "alışveriş",
        "outdoor", "açık hava", "business", "iş", "legislative",
        "meclis", "public", "kamu", "classic", "klasik", "auto",
        "otomobil", "lifestyle", "yaşam", "religious", "din",
        "general", "genel", "entertainment", "eğlence"
    )

    private val MOVIE_TITLE_KEYWORDS = setOf(
        "movie", "movies", "film", "films", "sinema", "cinema",
        "vod", "video on demand", "filmler", "filmleri"
    )

    private val SERIES_TITLE_KEYWORDS = setOf(
        "series", "dizi", "tv show", "tv shows", "shows",
        "diziler", "episode", "bölüm", "sezon", "season",
        "sitcom"
    )

    private val LIVE_URL_PATTERNS = listOf(
        "/live/", "live.php", "type=live", "/live_", "ch=", "channel="
    )

    private val MOVIE_URL_PATTERNS = listOf(
        "/movie/", "/movies/", "/vod/", "/vods/", "type=movie",
        "/film/", "/films/", ".mp4", ".mkv", ".avi"
    )

    private val SERIES_URL_PATTERNS = listOf(
        "/series/", "type=series", "/episode/", "season=", "sezon=",
        "/dizi/", "/diziler/"
    )

    private val VOD_EXTENSIONS = setOf("mp4", "mkv", "avi", "mov", "wmv", "flv", "webm", "mpg", "mpeg", "3gp")

    fun detect(groupTitle: String?, streamUrl: String?, channelName: String? = null): ContentType {
        val title = groupTitle?.lowercase()?.trim() ?: ""
        val url = streamUrl?.lowercase()?.trim() ?: ""
        val name = channelName?.lowercase()?.trim() ?: ""

        if (title.isNotBlank() && title !in AMBIGUOUS_TITLES) {
            val titleResult = detectByTitle(title)
            if (titleResult != ContentType.UNKNOWN) return titleResult
        }

        val urlResult = detectByUrl(url)
        if (urlResult != ContentType.UNKNOWN) return urlResult

        val extResult = detectByExtension(url)
        if (extResult != ContentType.UNKNOWN) return extResult

        val nameResult = detectByName(name)
        if (nameResult != ContentType.UNKNOWN) return nameResult

        return ContentType.LIVE
    }

    private fun detectByTitle(title: String): ContentType {
        val words = title.split(Regex("[\\s,/&();:]+")).map { it.trim().lowercase() }.filter { it.isNotEmpty() }
        for (word in words) {
            when {
                word in MOVIE_TITLE_KEYWORDS -> return ContentType.MOVIE
                word in SERIES_TITLE_KEYWORDS -> return ContentType.SERIES
                word in LIVE_TITLE_KEYWORDS -> return ContentType.LIVE
            }
        }
        return ContentType.UNKNOWN
    }

    private fun detectByUrl(url: String): ContentType {
        for (pattern in MOVIE_URL_PATTERNS) { if (url.contains(pattern)) return ContentType.MOVIE }
        for (pattern in SERIES_URL_PATTERNS) { if (url.contains(pattern)) return ContentType.SERIES }
        for (pattern in LIVE_URL_PATTERNS) { if (url.contains(pattern)) return ContentType.LIVE }
        return ContentType.UNKNOWN
    }

    private fun detectByExtension(url: String): ContentType {
        if (!url.contains(".")) return ContentType.UNKNOWN
        val ext = url.substringAfterLast(".").substringBefore("?").substringBefore("/").lowercase()
        if (ext in VOD_EXTENSIONS) return ContentType.MOVIE
        return ContentType.UNKNOWN
    }

    private fun detectByName(name: String): ContentType {
        if (name.contains("canli") || name.contains("canlı") || name.contains(" live") || name.contains(" tv ") ||
            name.startsWith("tv ") || name.contains(" radio") || name.contains("radyo") || name.contains(" fm")) {
            return ContentType.LIVE
        }
        return ContentType.UNKNOWN
    }
}