package com.c32pr559.storyverse.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.c32pr559.storyverse.local.entity.RecommendedTitle

@Database(entities = [RecommendedTitle::class], version = 1)
abstract class RecommendDatabase : RoomDatabase() {
    abstract fun recommendedTitleDao(): RecommendedTitleDao

    companion object {
        @Volatile
        private var instance: RecommendDatabase? = null

        fun getInstance(context: Context): RecommendDatabase {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    RecommendDatabase::class.java,
                    "app-database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = database
                database
            }
        }
    }

    fun clearDatabase() {
        val recommendedTitleDao = recommendedTitleDao()
        recommendedTitleDao.deleteAll()
    }
}