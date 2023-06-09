package com.example.storyapprakha.ui

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapprakha.data.network.responses.ListStoryItem
import com.example.storyapprakha.data.network.retrofit.Service
import com.example.storyapprakha.data.user.UserPreference

class StoryRepository(private val apiService: Service, private val userPreference: UserPreference) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                Paging(apiService, userPreference)
            }
        ).liveData
    }
}