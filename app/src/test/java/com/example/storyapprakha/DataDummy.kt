package com.example.storyapprakha

import com.example.storyapprakha.data.network.responses.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1684042679488_SUU4rBoi.jpg",
                "2023-05-14T05:37:59.490Z",
                "rakha",
                "1",
                null,
                "story-3LSSS6tXII-A4x-1",
                null
            )
            items.add(story)
        }
        return items
    }
}

//"listStory":[{"id":"story-3LSSS6tXII-A4x-1","name":"rakha","description":"1","photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1684042679488_SUU4rBoi.jpg","createdAt":"2023-05-14T05:37:59.490Z","lat":null,"lon":null}