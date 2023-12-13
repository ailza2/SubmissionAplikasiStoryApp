package com.dicoding.submissionaplikasistoryapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_story")
data class RemoteKey (
    @PrimaryKey val id: String,
    val prevKey : Int?,
    val nextKey : Int?
)