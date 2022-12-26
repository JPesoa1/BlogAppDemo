package com.example.blogappdemo.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

//Primer Paso
data class Post(
    val profile_picture: String = "",
    val profile_name: String = "",
    @ServerTimestamp
    var createdAt: Date? = null,
    val post_image: String = "",
    val post_description: String = "",
    val uid: String="")