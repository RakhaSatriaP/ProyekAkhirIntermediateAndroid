package com.example.storyapprakha.data.network.retrofit

import com.example.storyapprakha.data.network.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories?location=1")
    fun Loc(@Header("Authorization") token: String): Call<GetAllResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ResponseStory>

    @GET("stories")
    suspend fun getAll(@Header("Authorization") token: String, @Query("page") page: Int, @Query("size") size : Int):GetAllResponse

    @GET("stories/{id}")
    fun detail(@Header("Authorization") token: String, @Path("id") id: String): Call<DetailResponse>
}