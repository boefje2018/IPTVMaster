package com.iptv.master.domain.model

data class MediaItem(
    val id: Long,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val genreIds: List<Int>?,
    val originalLanguage: String?,
    val popularity: Double,
    val mediaType: MediaType,
    val genres: List<String>? = null,
    val runtime: Int? = null,
    val status: String? = null,
    val tagline: String? = null,
    val cast: List<CastMember>? = null,
    val trailerKey: String? = null,
    val numberOfSeasons: Int? = null,
    val numberOfEpisodes: Int? = null,
    val seasons: List<SeasonInfo>? = null,
    val networks: List<String>? = null,
    val createdBy: List<String>? = null
)

enum class MediaType {
    MOVIE, TV
}

data class CastMember(
    val id: Long,
    val name: String,
    val character: String?,
    val profileUrl: String?,
    val order: Int
)

data class SeasonInfo(
    val id: Long,
    val name: String,
    val overview: String?,
    val posterUrl: String?,
    val seasonNumber: Int,
    val episodeCount: Int?,
    val airDate: String?
)

data class EpisodeInfo(
    val id: Long,
    val name: String,
    val overview: String?,
    val stillUrl: String?,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val airDate: String?,
    val voteAverage: Double?
)

enum class MediaCategory(val displayName: String) {
    TRENDING_MOVIES("Trend Filmler"),
    TRENDING_TV("Trend Diziler"),
    POPULAR_MOVIES("Popüler Filmler"),
    POPULAR_TV("Popüler Diziler"),
    NOW_PLAYING("Vizyondakiler"),
    TOP_RATED_MOVIES("En İyi Filmler"),
    TOP_RATED_TV("En İyi Diziler"),
    UPCOMING("Yakında"),
    AIRING_TODAY("Bugün Yayında"),
    ON_THE_AIR("Havada")
}
