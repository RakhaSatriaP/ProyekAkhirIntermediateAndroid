package com.example.storyapprakha.ui

import android.util.Log
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapprakha.data.network.responses.GetAllResponse
import com.example.storyapprakha.data.network.responses.ListStoryItem
import com.example.storyapprakha.data.network.retrofit.Config
import com.example.storyapprakha.data.user.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val userPreference: UserPreference) : ViewModel() {
    private val token = runBlocking {
        userPreference.getToken().map {
            it
        }.first()
    }
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    fun getLoc() {
        Log.d("MainViewModel", token)
        _isLoading.value = true
        val client = Config.getApiService().Loc("Bearer $token")
        client.enqueue(object : Callback<GetAllResponse> {
            override fun onResponse(
                call: Call<GetAllResponse>,
                response: Response<GetAllResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _listStory.value = responseBody?.listStory
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<GetAllResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    init {
        getLoc()
    }
}