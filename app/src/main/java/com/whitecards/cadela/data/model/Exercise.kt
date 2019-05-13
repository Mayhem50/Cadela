package com.whitecards.cadela.data.model

import com.google.firebase.database.PropertyName

class Exercise {
    @PropertyName("name")
    lateinit var name: String
    @PropertyName("video_url")
    lateinit var videoUrl: String
    @PropertyName("image")
    lateinit var image: String
    @PropertyName("description")
    lateinit var description: String
    @PropertyName("difficulty")
    var difficulty: Int = 1
    val hasVideo: Boolean
        get() {
            return videoUrl.isNullOrEmpty()
        }
    val isLowerBodyPart: Boolean
        get() {
            return name.contains("E", true) || name.contains("F", true)
        }

    override fun equals(other: Any?): Boolean {
        if (this == other) return true
        if (other?.javaClass != javaClass) return false

        other as Exercise
        return name == other.name
    }
}