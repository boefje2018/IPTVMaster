package com.iptv.master.domain.usecase

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.iptv.master.domain.model.AppUpdate
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CheckUpdateUseCase @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    companion object {
        private const val GITHUB_API_URL = "https://api.github.com/repos/tahirhanci/IPTV-Master/releases/latest"
        private const val CURRENT_VERSION = "1.0.0"
    }

    suspend operator fun invoke(): Result<AppUpdate> {
        return try {
            val client = okHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
            val request = Request.Builder()
                .url(GITHUB_API_URL)
                .header("Accept", "application/vnd.github.v3+json")
                .build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return Result.failure(Exception("GitHub API returned ${response.code}"))
            }
            val body = response.body?.string() ?: return Result.failure(Exception("Empty response"))
            val json = gson.fromJson(body, JsonObject::class.java)
            val latestVersion = json.get("tag_name")?.asString?.removePrefix("v") ?: return Result.failure(Exception("No version found"))
            if (compareVersions(latestVersion, CURRENT_VERSION) <= 0) {
                return Result.failure(Exception("No update available"))
            }
            val downloadUrl = json.get("html_url")?.asString ?: ""
            val changelog = json.get("body")?.asString ?: ""
            val isMandatory = json.get("prerelease")?.asBoolean == false
            Result.success(
                AppUpdate(
                    latestVersion = latestVersion,
                    downloadUrl = downloadUrl,
                    changelog = changelog,
                    isMandatory = isMandatory
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun compareVersions(v1: String, v2: String): Int {
        val parts1 = v1.split(".").map { it.toIntOrNull() ?: 0 }
        val parts2 = v2.split(".").map { it.toIntOrNull() ?: 0 }
        val maxLen = maxOf(parts1.size, parts2.size)
        for (i in 0 until maxLen) {
            val p1 = parts1.getOrElse(i) { 0 }
            val p2 = parts2.getOrElse(i) { 0 }
            if (p1 != p2) return p1 - p2
        }
        return 0
    }
}
