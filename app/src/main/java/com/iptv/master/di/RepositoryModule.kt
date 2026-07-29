package com.iptv.master.di

import com.iptv.master.data.repository.ChannelRepositoryImpl
import com.iptv.master.data.repository.EPGRepositoryImpl
import com.iptv.master.data.repository.GitHubRepositoryImpl
import com.iptv.master.data.repository.PlaylistRepositoryImpl
import com.iptv.master.data.repository.SettingsRepositoryImpl
import com.iptv.master.domain.repository.ChannelRepository
import com.iptv.master.domain.repository.EPGRepository
import com.iptv.master.domain.repository.GitHubRepository
import com.iptv.master.domain.repository.PlaylistRepository
import com.iptv.master.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChannelRepository(impl: ChannelRepositoryImpl): ChannelRepository

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(impl: PlaylistRepositoryImpl): PlaylistRepository

    @Binds
    @Singleton
    abstract fun bindEPGRepository(impl: EPGRepositoryImpl): EPGRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindGitHubRepository(impl: GitHubRepositoryImpl): GitHubRepository
}
