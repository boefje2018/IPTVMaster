package com.iptv.master.data.repository

import com.iptv.master.data.remote.TmdbApi
import com.iptv.master.data.remote.dto.CastDto
import com.iptv.master.data.remote.dto.TmdbMovieDetailDto
import com.iptv.master.data.remote.dto.TmdbTvDetailDto
import com.iptv.master.domain.model.CastMember
import com.iptv.master.domain.model.EpisodeInfo
import com.iptv.master.domain.model.MediaCategory
import com.iptv.master.domain.model.MediaItem
import com.iptv.master.domain.model.MediaType
import com.iptv.master.domain.model.SeasonInfo
import com.iptv.master.domain.repository.TmdbRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : TmdbRepository {

    override suspend fun getMediaByCategory(category: MediaCategory, page: Int): List<MediaItem> {
        return when (category) {
            MediaCategory.TRENDING_MOVIES -> tmdbApi.getTrendingMovies(page = page).results.map { it.toMediaItem(MediaType.MOVIE) }
            MediaCategory.TRENDING_TV -> tmdbApi.getTrendingTv(page = page).results.map { it.toMediaItem(MediaType.TV) }
            MediaCategory.POPULAR_MOVIES -> tmdbApi.getPopularMovies(page = page).results.map { it.toMediaItem(MediaType.MOVIE) }
            MediaCategory.POPULAR_TV -> tmdbApi.getPopularTv(page = page).results.map { it.toMediaItem(MediaType.TV) }
            MediaCategory.NOW_PLAYING -> tmdbApi.getNowPlayingMovies(page = page).results.map { it.toMediaItem(MediaType.MOVIE) }
            MediaCategory.TOP_RATED_MOVIES -> tmdbApi.getTopRatedMovies(page = page).results.map { it.toMediaItem(MediaType.MOVIE) }
            MediaCategory.TOP_RATED_TV -> tmdbApi.getTopRatedTv(page = page).results.map { it.toMediaItem(MediaType.TV) }
            MediaCategory.UPCOMING -> tmdbApi.getUpcomingMovies(page = page).results.map { it.toMediaItem(MediaType.MOVIE) }
            MediaCategory.AIRING_TODAY -> tmdbApi.getAiringTodayTv(page = page).results.map { it.toMediaItem(MediaType.TV) }
            MediaCategory.ON_THE_AIR -> tmdbApi.getOnTheAirTv(page = page).results.map { it.toMediaItem(MediaType.TV) }
        }
    }

    override suspend fun getMovieDetail(movieId: Long): MediaItem {
        val detail = tmdbApi.getMovieDetail(movieId)
        val credits = tmdbApi.getMovieCredits(movieId)
        val videos = tmdbApi.getMovieVideos(movieId)
        return detail.toMediaItem(credits.cast, videos.results.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }?.key)
    }

    override suspend fun getTvDetail(tvId: Long): MediaItem {
        val detail = tmdbApi.getTvDetail(tvId)
        val credits = tmdbApi.getTvCredits(tvId)
        val videos = tmdbApi.getTvVideos(tvId)
        return detail.toMediaItem(credits.cast, videos.results.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }?.key)
    }

    override suspend fun search(query: String, page: Int): List<MediaItem> {
        val movies = tmdbApi.searchMovies(query, page = page).results.map { it.toMediaItem(MediaType.MOVIE) }
        val tv = tmdbApi.searchTv(query, page = page).results.map { it.toMediaItem(MediaType.TV) }
        return movies + tv
    }

    override suspend fun searchMovies(query: String, page: Int): List<MediaItem> {
        return tmdbApi.searchMovies(query, page = page).results.map { it.toMediaItem(MediaType.MOVIE) }
    }

    override suspend fun searchTv(query: String, page: Int): List<MediaItem> {
        return tmdbApi.searchTv(query, page = page).results.map { it.toMediaItem(MediaType.TV) }
    }

    private fun com.iptv.master.data.remote.dto.TmdbMovieDto.toMediaItem(type: MediaType) = MediaItem(
        id = id, title = title, overview = overview,
        posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        backdropUrl = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" },
        releaseDate = releaseDate, voteAverage = voteAverage, voteCount = voteCount,
        genreIds = genreIds, originalLanguage = originalLanguage, popularity = popularity,
        mediaType = type
    )

    private fun com.iptv.master.data.remote.dto.TmdbTvDto.toMediaItem(type: MediaType) = MediaItem(
        id = id, title = name, overview = overview,
        posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        backdropUrl = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" },
        releaseDate = firstAirDate, voteAverage = voteAverage, voteCount = voteCount,
        genreIds = genreIds, originalLanguage = originalLanguage, popularity = popularity,
        mediaType = type
    )

    private fun TmdbMovieDetailDto.toMediaItem(cast: List<CastDto>? = null, trailerKey: String? = null) = MediaItem(
        id = id, title = title, overview = overview,
        posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        backdropUrl = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" },
        releaseDate = releaseDate, voteAverage = voteAverage, voteCount = voteCount,
        genreIds = null, originalLanguage = originalLanguage, popularity = popularity,
        mediaType = MediaType.MOVIE, genres = genres?.map { it.name },
        runtime = runtime, status = status, tagline = tagline,
        cast = cast?.sortedBy { it.order }?.take(20)?.map { it.toCastMember() },
        trailerKey = trailerKey
    )

    private fun TmdbTvDetailDto.toMediaItem(cast: List<CastDto>? = null, trailerKey: String? = null) = MediaItem(
        id = id, title = name, overview = overview,
        posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        backdropUrl = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" },
        releaseDate = firstAirDate, voteAverage = voteAverage, voteCount = voteCount,
        genreIds = null, originalLanguage = originalLanguage, popularity = popularity,
        mediaType = MediaType.TV, genres = genres?.map { it.name },
        runtime = null, status = status, tagline = tagline,
        cast = cast?.sortedBy { it.order }?.take(20)?.map { it.toCastMember() },
        trailerKey = trailerKey, numberOfSeasons = numberOfSeasons,
        numberOfEpisodes = numberOfEpisodes, networks = networks?.map { it.name },
        createdBy = createdBy?.map { it.name },
        seasons = seasons?.map { SeasonInfo(
            id = it.id, name = it.name, overview = it.overview,
            posterUrl = it.posterPath?.let { p -> "https://image.tmdb.org/t/p/w500$p" },
            seasonNumber = it.seasonNumber, episodeCount = it.episodeCount, airDate = it.airDate
        )}
    )

    private fun CastDto.toCastMember() = CastMember(
        id = id, name = name, character = character,
        profileUrl = profilePath?.let { "https://image.tmdb.org/t/p/w185$it" },
        order = order
    )
}
