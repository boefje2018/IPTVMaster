package com.iptv.master.data.remote

import com.iptv.master.data.remote.dto.TmdbGenreListDto
import com.iptv.master.data.remote.dto.TmdbMovieCreditsDto
import com.iptv.master.data.remote.dto.TmdbMovieDetailDto
import com.iptv.master.data.remote.dto.TmdbMovieResponse
import com.iptv.master.data.remote.dto.TmdbSeasonDetailDto
import com.iptv.master.data.remote.dto.TmdbTvDetailDto
import com.iptv.master.data.remote.dto.TmdbTvResponse
import com.iptv.master.data.remote.dto.TmdbVideosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("trending/tv/week")
    suspend fun getTrendingTv(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbTvResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("tv/popular")
    suspend fun getPopularTv(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbTvResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTv(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbTvResponse

    @GET("tv/airing_today")
    suspend fun getAiringTodayTv(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbTvResponse

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTv(
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbTvResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "tr-TR"
    ): TmdbMovieDetailDto

    @GET("tv/{tv_id}")
    suspend fun getTvDetail(
        @Path("tv_id") tvId: Long,
        @Query("language") language: String = "tr-TR"
    ): TmdbTvDetailDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "tr-TR"
    ): TmdbMovieCreditsDto

    @GET("tv/{tv_id}/credits")
    suspend fun getTvCredits(
        @Path("tv_id") tvId: Long,
        @Query("language") language: String = "tr-TR"
    ): TmdbMovieCreditsDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "tr-TR"
    ): TmdbVideosResponse

    @GET("tv/{tv_id}/videos")
    suspend fun getTvVideos(
        @Path("tv_id") tvId: Long,
        @Query("language") language: String = "tr-TR"
    ): TmdbVideosResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("search/tv")
    suspend fun searchTv(
        @Query("query") query: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbTvResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "tr-TR"
    ): TmdbGenreListDto

    @GET("genre/tv/list")
    suspend fun getTvGenres(
        @Query("language") language: String = "tr-TR"
    ): TmdbGenreListDto

    @GET("discover/movie")
    suspend fun discoverMoviesByGenre(
        @Query("with_genres") genreIds: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int = 1
    ): TmdbMovieResponse

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getSeasonDetail(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Query("language") language: String = "tr-TR"
    ): TmdbSeasonDetailDto
}
