package com.c32pr559.storyverse.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendedtitles")
data class RecommendedTitle(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String
)