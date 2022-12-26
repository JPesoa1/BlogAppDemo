package com.example.blogappdemo.presentation.camera

import android.accounts.AuthenticatorDescription
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogappdemo.core.Result
import com.example.blogappdemo.domain.auth.AuthRepo
import com.example.blogappdemo.domain.camera.CameraRepo
import com.example.blogappdemo.presentation.auth.AuthViewModel
import kotlinx.coroutines.Dispatchers

class CameraViewModel(private val repo: CameraRepo): ViewModel() {

    fun uploadPhoto(imageBitmap: Bitmap, description: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.uploadPhoto(imageBitmap,description)))
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }
}

class CameraViewModelFactory(private val repo: CameraRepo): ViewModelProvider.Factory{
    //otra forma de generar el viewmodel en el return
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CameraViewModel(repo) as T
    }
}