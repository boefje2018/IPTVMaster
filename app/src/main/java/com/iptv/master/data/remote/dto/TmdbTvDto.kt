package com.iptv.master.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TmdbTvResponse(
    val page: Int,
    val results: List<TmdbTvDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class TmdbTvDto(
    val id: Long,
    val name: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("original_language") val originalLanguage: String?,
    val popularity: Double,
    @SerializedName("origin_country") val originCountry: List<String>?
)

data class TmdbTvDetailDto(
    val id: Long,
    val name: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("last_air_date") val lastAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    val status: String?,
    val tagline: String?,
    val homepage: String?,
    val genres: List<GenreDto>?,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int?,
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int?,
    @SerializedName("created_by") val createdBy: List<CreatedByDto>?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyDto>?,
    @SerializedName("spoken_languages") val spokenLanguages: List<LanguageDto>?,
    @SerializedName("origin_country") val originCountry: List<String>?,
    val adult: Boolean,
    @SerializedName("original_name") val originalName: String?,
    val popularity: Double,
    val networks: List<NetworkDto>?,
    val seasons: List<SeasonDto>?,
    @SerializedName("in_production") val inProduction: Boolean?,
    val type: String?
)

data class CreatedByDto(
    val id: Long,
    val name: String,
    @SerializedName("profile_path") val profilePath: String?,
    val gender: Int?
)

data class NetworkDto(
    val id: Int,
    val name: String,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("origin_country") val originCountry: String?
)

data class SeasonDto(
    val id: Long,
    val name: String,
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("air_date") val airDate: String?,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("episode_count") val episodeCount: Int?,
    @SerializedName("vote_average") val voteAverage: Double?
)

data class TmdbEpisodeDto(
    val id: Long,
    val name: String,
    val overview: String?,
    @SerializedName("still_path") val stillPath: String?,
    @SerializedName("air_date") val airDate: String?,
    @SerializedName("episode_number") val episodeNumber: Int,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("runtime") val runtime: Int?
)

data class TmdbSeasonDetailDto(
    val id: Long,
    @SerializedName("air_date") val airDate: String?,
    val name: String,
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("season_number") val seasonNumber: Int,
    val episodes: List<TmdbEpisodeDto>?
)
