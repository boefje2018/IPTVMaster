package com.iptv.master.domain.repository

import com.iptv.master.domain.model.MediaCategory
import com.iptv.master.domain.model.MediaItem

interface TmdbRepository {
    suspend fun getMediaByCategory(category: MediaCategory, page: Int = 1): List<MediaItem>
    suspend fun getMovieDetail(movieId: Long): MediaItem
    suspend fun getTvDetail(tvId: Long): MediaItem
    suspend fun search(query: String, page: Int = 1): List<MediaItem>
    suspend fun searchMovies(query: String, page: Int = 1): List<MediaItem>
    suspend fun searchTv(query: String, page: Int = 1): List<MediaItem>
}
