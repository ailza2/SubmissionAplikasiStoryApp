package com.dicoding.submissionaplikasistoryapp.data

import com.dicoding.submissionaplikasistoryapp.database.ListStory
import com.google.gson.annotations.SerializedName

class GetStoryResponse (

    @field:SerializedName("listStory")
    val listStory: List<ListStory>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

)