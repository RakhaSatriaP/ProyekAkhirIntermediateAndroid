package com.example.storyapprakha.di

import android.content.Context
import com.example.storyapprakha.data.network.retrofit.Config
import com.example.storyapprakha.data.user.UserPreference
import com.example.storyapprakha.ui.StoryDatabase
import com.example.storyapprakha.ui.StoryRepository

object Injection {
    fun provideRepository(pref: UserPreference): StoryRepository {
        val apiService = Config.getApiService()
        return StoryRepository(apiService, pref)
    }
}