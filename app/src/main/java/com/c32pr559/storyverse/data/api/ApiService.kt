package com.c32pr559.storyverse.data.api

import com.c32pr559.storyverse.data.api.model.StoryResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("dataset")
    suspend fun getStory():List<StoryResponse>

    companion object{
        var apiService: ApiService? = null
        fun getInstance() : ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://backend-dot-storyverse-app.et.r.appspot.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}