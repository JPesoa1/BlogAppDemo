package com.example.blogappdemo.data.model


import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

//Primer Paso
data class Post(
    @Exclude @JvmField
    val id: String = "",
    val profile_name: String = "",
    @ServerTimestamp
    var createdAt: Date? = null,
    val post_image: String = "",
    val post_description: String = "",
    val poster: Poster? = null,
    val likes: Long = 0,
    @Exclude @JvmField //nos sirve para no traer nada de firebase el atributo liked o enviarlo solo para uso local
    var liked: Boolean = false
)

data class Poster(
    val username: String? = "",
    val uid: String? = null,
    val profile_picture: String = ""
)