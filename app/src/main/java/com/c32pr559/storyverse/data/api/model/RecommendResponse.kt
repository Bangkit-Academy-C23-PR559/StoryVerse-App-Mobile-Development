package com.c32pr559.storyverse.data.api.model

import com.google.gson.annotations.SerializedName

data class RecommendResponse(
    @SerializedName("recommended_titles")
    val recommendedTitles: List<String>
)