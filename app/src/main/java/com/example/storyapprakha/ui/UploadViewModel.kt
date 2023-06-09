package com.example.storyapprakha.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapprakha.data.network.responses.ResponseStory
import com.example.storyapprakha.data.network.retrofit.Config
import com.example.storyapprakha.data.user.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UploadViewModel(private val userPreference: UserPreference) : ViewModel() {
    private val token = runBlocking {
        userPreference.getToken().map {
            it
        }.first()
    }
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean?>()
    val isError: LiveData<Boolean?> = _isError

    fun upload(photo: File?, desc: String) {
        _isLoading.value = true
        if (photo != null) {
            val descBody = desc.toRequestBody("text/plain".toMediaType())
            val photoBody = photo.asRequestBody("image/jpeg".toMediaType())
            val photoMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                photo.name,
                photoBody
            )
            val client =
                Config.getApiService().addNewStory("Bearer $token", descBody, photoMultiPart)
            client.enqueue(object : Callback<ResponseStory> {
                override fun onResponse(
                    call: Call<ResponseStory>,
                    response: Response<ResponseStory>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.error == false) {
                            _isLoading.value = false
                            _isError.value = false

                        } else {
                            _isLoading.value = false
                            _isError.value = true
                        }
                    } else {
                        _isLoading.value = false
                        _isError.value = true
                    }

                }

                override fun onFailure(call: Call<ResponseStory>, t: Throwable) {
                    Log.e("UploadViewModel", "onFailure: ${t.message}")
                }

            })
        }

    }
}