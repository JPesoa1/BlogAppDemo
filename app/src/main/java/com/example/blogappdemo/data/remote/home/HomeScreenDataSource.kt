package com.example.blogappdemo.data.remote.home

import com.example.blogappdemo.core.Result
import com.example.blogappdemo.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {
    suspend fun getLatesPosts(): Flow<Result<List<Post>>> = callbackFlow {
        val postList = mutableListOf<Post>()

        var postReference: Query? = null

        try {
            postReference = FirebaseFirestore.getInstance().collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)

        } catch (e: Throwable) {
            close(e)
        }

        val suscription = postReference?.addSnapshotListener { value, error ->
            if (value == null) return@addSnapshotListener

            try {
                postList.clear()
                for (post in value.documents) {
                    post.toObject(Post::class.java)?.let {
                        it.apply {
                            createdAt = post.getTimestamp(
                                "createdAt",
                                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                            )?.toDate()
                        }
                        //it.aplly antes de añadir el objeto a la lista mutable lo que hacemos es al atributo createdAt poenrlo la fecha de creacion en caso de nulo o por el delay ,porque despues de un segundo se pondra el tiempo correcto
                        postList.add(it)
                    }
                }

            } catch (e: Exception) {
                close(e)
            }

            trySend(Result.Success(postList)).isSuccess
        }

        awaitClose { suscription?.remove() } //para parar las request

        //postList.sortByDescending {
        //it.createdAt //esto indica por que atributo lo ordenamos, es decir a nuestra array postlist lo ordenamos de forma descendiente // en caso de tener miles de post esto satura la palicacion y tarda
        //}

    }
}