package com.example.blogappdemo.data.remote.camera

import android.graphics.Bitmap
import com.example.blogappdemo.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

class CameraDataSource {
    suspend fun uploadPhoto(imageBitmap: Bitmap, description: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val randoName = UUID.randomUUID().toString() //nos genera un nombre random
        val imageRef = FirebaseStorage.getInstance().reference.child("${user?.uid}/post/$randoName")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downloadUrl = imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await()
            .toString()  //esto sube la foto a firebase en la carpeta y obtiene la url dedescargar y la devuelve


        user?.let {
            it.displayName?.let { displayName ->
                FirebaseFirestore.getInstance().collection("posts").add(
                    Post(
                        profile_name = displayName,
                        profile_picture = it.photoUrl.toString(),
                        post_image = downloadUrl,
                        post_description = description,
                        uid= user.uid
                    )
                )
            }

        }


    }
}