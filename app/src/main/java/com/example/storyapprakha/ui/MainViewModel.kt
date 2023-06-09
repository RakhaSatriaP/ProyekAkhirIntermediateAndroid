package com.example.storyapprakha.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapprakha.data.network.responses.GetAllResponse
import com.example.storyapprakha.data.network.responses.ListStoryItem
import com.example.storyapprakha.data.network.retrofit.Config
import com.example.storyapprakha.data.user.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val userPreference: UserPreference, private val repository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)


    fun getToken(): LiveData<String> {
        return userPreference.getToken().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userPreference.deleteToken()
        }
    }


}
