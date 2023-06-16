package com.c32pr559.storyverse.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.c32pr559.storyverse.data.api.ApiService
import com.c32pr559.storyverse.data.api.model.StoryResponse
import com.c32pr559.storyverse.local.entity.BookmarkStory
import com.c32pr559.storyverse.local.room.BookmarkDao
import com.c32pr559.storyverse.local.room.BookmarkDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application){
    var storyListResponse: List<StoryResponse> by mutableStateOf(listOf())
    var items: List<StoryResponse> by mutableStateOf(emptyList())
    var errorMessage: String by mutableStateOf("")
    private var bookmarkDao: BookmarkDao? = null
    private var bookmarkDB: BookmarkDatabase? = null

    init {
        bookmarkDB = BookmarkDatabase.getInstance(application)
        bookmarkDao = bookmarkDB?.bookmarkDao()
    }

    fun getStoryList(){
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val storyList = apiService.getStory()
                storyListResponse = storyList
            }
            catch (e:Exception){
                errorMessage = e.message.toString()
            }
        }
    }
    fun addToBookmark(id: Int, title: String, author: String, cover_image: String){
        CoroutineScope(Dispatchers.IO).launch {
            var story = BookmarkStory(id, title, author, cover_image)
            bookmarkDao?.addBookmark(story)
        }
    }
    fun checkUser(id: Int) = bookmarkDao?.checkUser(id)

    fun removeFromBookmark(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            bookmarkDao?.deleteBookmark(id)
        }
    }
}