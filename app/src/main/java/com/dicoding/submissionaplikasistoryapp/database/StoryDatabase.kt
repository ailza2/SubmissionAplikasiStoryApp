package com.dicoding.submissionaplikasistoryapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ListStory::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)

abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeyDao
}