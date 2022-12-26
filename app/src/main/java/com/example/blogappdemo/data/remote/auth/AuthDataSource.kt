package com.example.blogappdemo.data.remote.auth

import android.graphics.Bitmap
import android.net.Uri
import com.example.blogappdemo.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class AuthDataSource {
    suspend fun signIn(email:String,password:String) :FirebaseUser?{
        //Me retorna el usuario por su usuario y contraseña
        val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
        return  authResult.user
    }

    suspend fun signUp(email: String, password: String, username: String): FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await()
        //uid identificador unico del usuario
        authResult.user?.uid?.let {
            FirebaseFirestore.getInstance().collection("users").document(it).set(User(email,username,"FOTO_URL.PNG")).await()
        }
        return  authResult.user

    }

    suspend fun updateUserProfile(imageBitmap: Bitmap,username:String ){
        val user = FirebaseAuth.getInstance().currentUser
        val imageRef = FirebaseStorage.getInstance().reference.child("${user?.uid}/profile_picture")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val downloadUrl = imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()  //esto sube la foto a firebase en la carpeta y obtiene la url dedescargar y la devuelve
        //Aqui creo el username en displayname
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .setPhotoUri(Uri.parse(downloadUrl))
            .build()
        user?.updateProfile(profileUpdates)?.await()

    }
}