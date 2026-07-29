package com.iptv.master.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TmdbMovieResponse(
    val page: Int,
    val results: List<TmdbMovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class TmdbMovieDto(
    val id: Long,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("original_language") val originalLanguage: String?,
    val popularity: Double,
    val video: Boolean,
    val adult: Boolean
)

data class TmdbMovieDetailDto(
    val id: Long,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    val budget: Long?,
    val revenue: Long?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val homepage: String?,
    val genres: List<GenreDto>?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyDto>?,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountryDto>?,
    @SerializedName("spoken_languages") val spokenLanguages: List<LanguageDto>?,
    val imdb_id: String?,
    val adult: Boolean,
    @SerializedName("original_title") val originalTitle: String?,
    val popularity: Double
)

data class TmdbMovieCreditsDto(
    val id: Long,
    val cast: List<CastDto>,
    val crew: List<CrewDto>
)

data class TmdbGenreListDto(
    val genres: List<GenreDto>
)

data class GenreDto(
    val id: Int,
    val name: String
)

data class ProductionCompanyDto(
    val id: Int,
    val name: String,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("origin_country") val originCountry: String?
)

data class ProductionCountryDto(
    @SerializedName("iso_3166_1") val iso3166_1: String,
    val name: String
)

data class LanguageDto(
    @SerializedName("iso_639_1") val iso639_1: String,
    val name: String,
    @SerializedName("english_name") val englishName: String?
)

data class CastDto(
    val id: Long,
    val name: String,
    val character: String?,
    @SerializedName("profile_path") val profilePath: String?,
    val order: Int,
    val gender: Int?,
    val known_for_department: String?
)

data class CrewDto(
    val id: Long,
    val name: String,
    val job: String?,
    val department: String?,
    @SerializedName("profile_path") val profilePath: String?,
    val gender: Int?,
    val known_for_department: String?
)

data class TmdbVideoDto(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    @SerializedName("iso_639_1") val iso639_1: String
)

data class TmdbVideosResponse(
    val id: Long,
    val results: List<TmdbVideoDto>
)
