package com.c32pr559.storyverse.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.c32pr559.storyverse.local.entity.BookmarkStory
import com.c32pr559.storyverse.local.room.BookmarkDao
import com.c32pr559.storyverse.local.room.BookmarkDatabase

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    private var favoriteDao: BookmarkDao
    private var favoriteDatabase: BookmarkDatabase

    init {
        favoriteDatabase = BookmarkDatabase.getInstance(application)
        favoriteDao = favoriteDatabase.bookmarkDao()
    }

    fun getFavoriteUser(): LiveData<List<BookmarkStory>> {
        return favoriteDao.getBookmark()
    }
}