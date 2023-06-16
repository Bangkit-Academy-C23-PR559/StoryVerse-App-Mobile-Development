package com.c32pr559.storyverse.util

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.c32pr559.storyverse.data.api.model.RecommendResponse
import com.c32pr559.storyverse.local.entity.RecommendedTitle
import com.c32pr559.storyverse.local.room.RecommendDatabase
import com.c32pr559.storyverse.local.room.RecommendedTitleDao
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class RecommendRepository(
    private val application: Application
) {
    private val recommendedTitleDao: RecommendedTitleDao

    init {
        val database = RecommendDatabase.getInstance(application)
        recommendedTitleDao = database.recommendedTitleDao()
    }

    fun getRecommendedTitles(): LiveData<List<RecommendedTitle>> {
        return recommendedTitleDao.getRecommendedTitles()
    }

    suspend fun fetchDataFromServer(categories: List<String>) {
        val url = "https://storyverse-app.et.r.appspot.com/recommend"
        val mediaType = "application/json".toMediaType()

        val categoriesJson = Json.encodeToString(ListSerializer(String.serializer()), categories)
        val requestBody = """{"categories": $categoriesJson}""".toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        val call = client.newCall(request)

        val response = call.execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            val recommendedTitlesResponse = Gson().fromJson(responseBody, RecommendResponse::class.java)
            val recommendedTitles = recommendedTitlesResponse.recommendedTitles

            // Simpan data ke database Room
            withContext(Dispatchers.IO) {
                recommendedTitleDao.insertRecommendedTitles(recommendedTitles.mapIndexed { index, title ->
                    RecommendedTitle(id = index, title = title)
                })
            }
        } else {
            println("Request failed: ${response.code} ${response.message}")
            Log.d("Yang dikirim", "$categories")
        }
    }
}
