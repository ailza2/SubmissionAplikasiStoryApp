package com.dicoding.submissionaplikasistoryapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(Story: List<ListStory>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStory>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}