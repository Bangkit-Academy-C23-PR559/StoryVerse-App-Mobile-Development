package com.c32pr559.storyverse.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.c32pr559.storyverse.local.entity.BookmarkStory

@Dao
interface BookmarkDao {
    @Insert
    fun addBookmark(bookmarkStory: BookmarkStory)

    @Query("SELECT * FROM bookmark_story")
    fun getBookmark(): LiveData<List<BookmarkStory>>

    @Query("DELETE FROM bookmark_story WHERE bookmark_story.id = :id")
    fun deleteBookmark(id: Int)

    @Query("SELECT count(*) FROM bookmark_story WHERE bookmark_story.id = :id")
    fun checkUser(id: Int): Int
}