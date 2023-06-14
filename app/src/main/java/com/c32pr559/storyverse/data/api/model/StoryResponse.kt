package com.c32pr559.storyverse.data.api.model

import com.google.gson.annotations.SerializedName

data class StoryResponse(

	@field:SerializedName("CoverImage")
	val coverImage: String,

	@field:SerializedName("Category")
	val category: String,

	@field:SerializedName("Article")
	val article: String,

	@field:SerializedName("Title")
	val title: String,

	@field:SerializedName("Author")
	val author: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("Created_date")
	val createdDate: String,

	@field:SerializedName("Url")
	val url: String,

	val isFavorite: Boolean =  false
)