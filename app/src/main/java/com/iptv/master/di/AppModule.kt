package com.iptv.master.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.iptv.master.domain.model.DonationInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideDonationInfo(@ApplicationContext context: Context, gson: Gson): DonationInfo {
        return try {
            val json = context.assets.open("donation.json")
                .bufferedReader().use { it.readText() }
            gson.fromJson(json, DonationInfo::class.java)
        } catch (e: Exception) {
            DonationInfo(
                buyMeACoffee = "https://buymeacoffee.com/tahirhanci",
                paypal = "https://paypal.me/tahirhanci",
                githubSponsors = "https://github.com/sponsors/tahirhanci",
                bitcoin = "",
                ethereum = "",
                message = "Support the development of IPTV Master"
            )
        }
    }

    @Provides
    @Singleton
    fun provideConstants(): Constants = Constants()
}

class Constants {
    val appName: String = "IPTV Master"
    val appVersion: String = "1.0.0"
    val githubRepo: String = "tahirhanci/IPTV-Master"
    val githubUrl: String = "https://github.com/tahirhanci/IPTV-Master"
    val defaultUserAgent: String = "IPTVMaster/1.0.0"
}
