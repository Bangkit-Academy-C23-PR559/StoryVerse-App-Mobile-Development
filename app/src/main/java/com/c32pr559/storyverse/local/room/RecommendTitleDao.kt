package com.c32pr559.storyverse.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.c32pr559.storyverse.local.entity.RecommendedTitle

@Dao
interface RecommendedTitleDao {
    @Query("SELECT * FROM recommendedtitles")
    fun getRecommendedTitles(): LiveData<List<RecommendedTitle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendedTitles(titles: List<RecommendedTitle>)

    @Query("DELETE FROM recommendedtitles")
    fun deleteAll()
}