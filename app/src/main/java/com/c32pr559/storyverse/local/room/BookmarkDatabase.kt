package com.c32pr559.storyverse.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.c32pr559.storyverse.local.entity.BookmarkStory

@Database(entities = [BookmarkStory::class], version = 2, exportSchema = true)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var instance: BookmarkDatabase? = null

        fun getInstance(context: Context): BookmarkDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java, "bookmark.db"
                ).fallbackToDestructiveMigration().build().also {
                    instance = it
                }
            }
        }
    }
}