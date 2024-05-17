package com.nelvari.storyapp

import com.nelvari.storyapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photo",
                "create",
                "name",
                "desc",
                i.toString(),
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }

}