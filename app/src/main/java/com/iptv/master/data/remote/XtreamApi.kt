package com.iptv.master.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface XtreamApi {
    @GET("{username}/{password}/live")
    suspend fun getLiveStreams(
        @Path("username") username: String,
        @Path("password") password: String
    ): ResponseBody

    @GET("{username}/{password}/live/{categoryId}")
    suspend fun getLiveStreamsByCategory(
        @Path("username") username: String,
        @Path("password") password: String,
        @Path("categoryId") categoryId: String
    ): ResponseBody

    @GET("{username}/{password}/live_categories")
    suspend fun getLiveCategories(
        @Path("username") username: String,
        @Path("password") password: String
    ): ResponseBody

    @GET("{username}/{password}/vod_categories")
    suspend fun getVodCategories(
        @Path("username") username: String,
        @Path("password") password: String
    ): ResponseBody

    @GET("{username}/{password}/series_categories")
    suspend fun getSeriesCategories(
        @Path("username") username: String,
        @Path("password") password: String
    ): ResponseBody

    @GET("{username}/{password}/vod")
    suspend fun getVodStreams(
        @Path("username") username: String,
        @Path("password") password: String
    ): ResponseBody

    @GET("{username}/{password}/vod/{categoryId}")
    suspend fun getVodStreamsByCategory(
        @Path("username") username: String,
        @Path("password") password: String,
        @Path("categoryId") categoryId: String
    ): ResponseBody

    @GET("{username}/{password}/series")
    suspend fun getSeriesStreams(
        @Path("username") username: String,
        @Path("password") password: String
    ): ResponseBody

    @GET("{username}/{password}/series/{categoryId}")
    suspend fun getSeriesStreamsByCategory(
        @Path("username") username: String,
        @Path("password") password: String,
        @Path("categoryId") categoryId: String
    ): ResponseBody

    @GET("player_api.php")
    suspend fun getPlayerInfo(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "user_info"
    ): ResponseBody

    @GET("player_api.php")
    suspend fun getAllVod(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_vod_streams"
    ): ResponseBody

    @GET("player_api.php")
    suspend fun getAllSeries(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_series"
    ): ResponseBody

    @GET("player_api.php")
    suspend fun getVodInfo(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_vod_info",
        @Query("vod_id") vodId: String
    ): ResponseBody

    @GET("player_api.php")
    suspend fun getSeriesInfo(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_series_info",
        @Query("series_id") seriesId: String
    ): ResponseBody

    @GET("player_api.php")
    suspend fun getShortEpg(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_short_epg",
        @Query("stream_id") streamId: String
    ): ResponseBody
}
