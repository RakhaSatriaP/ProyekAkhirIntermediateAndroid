package com.example.storyapprakha.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapprakha.data.network.responses.LoginResponse
import com.example.storyapprakha.data.network.retrofit.Config
import com.example.storyapprakha.data.user.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userPreference: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean?>()
    val isError: LiveData<Boolean?> = _isError

    fun login(email: String, password: String) {
        _isLoading.value = true
        _isError.value = null
        val client = Config.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.error == false) {
                        _isLoading.value = false
                        _isError.value = false
                        val result = responseBody.loginResult
                        viewModelScope.launch {
                            userPreference.saveToken(result.token)
                            _isLoading.value = false
                            _isError.value = false
                        }
                    } else {
                        _isLoading.value = false
                        _isError.value = true
                    }
                } else {
                    _isLoading.value = false
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginViewModel", "onFailure: ${t.message}")
            }

        })
    }

}