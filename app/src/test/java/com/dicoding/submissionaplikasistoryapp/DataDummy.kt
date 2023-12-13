package com.dicoding.submissionaplikasistoryapp

import com.dicoding.submissionaplikasistoryapp.database.ListStory

object DataDummy {

    fun generateDummyQuoteResponse(): List<ListStory> {
        val items: MutableList<ListStory> = arrayListOf()
        for (i in 0..100) {
            val story = ListStory(
                i.toString(),
                "created at + $i",
                "name $i",
                "description $i",
                i.toDouble(),
                "id : $i",
                i.toDouble(),
            )
            items.add(story)
        }
        return items
    }
}