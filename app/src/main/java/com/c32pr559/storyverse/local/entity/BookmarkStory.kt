package com.c32pr559.storyverse.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "bookmark_story")
data class BookmarkStory (
    @PrimaryKey
    val id: Int,
    val title: String,
    val author:String,
    val cover_image: String
    ) : Serializable