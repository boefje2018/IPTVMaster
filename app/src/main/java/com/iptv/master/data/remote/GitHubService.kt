package com.iptv.master.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface GitHubService {
    @GET
    suspend fun fetchRawContent(@Url url: String): ResponseBody
}
