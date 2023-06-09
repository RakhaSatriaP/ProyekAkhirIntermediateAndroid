package com.example.storyapprakha.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapprakha.data.network.responses.DetailResponse
import com.example.storyapprakha.data.network.responses.Story
import com.example.storyapprakha.data.network.retrofit.Config
import com.example.storyapprakha.data.user.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val userPreference: UserPreference) : ViewModel() {
    private val token = runBlocking {
        userPreference.getToken().map {
            it
        }.first()
    }
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detail = MutableLiveData<Story>()
    val detail: LiveData<Story> = _detail

    fun getDetail(id: String) {
        _isLoading.value = true
        val client = Config.getApiService().detail("Bearer $token", id)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detail.value = responseBody?.story
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e("DetailViewModel", "onFailure: ${t.message}")
            }

        })
    }
}