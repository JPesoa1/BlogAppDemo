package com.example.blogappdemo.ui.camera

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogappdemo.R
import com.example.blogappdemo.core.Result
import com.example.blogappdemo.data.remote.camera.CameraDataSource
import com.example.blogappdemo.data.remote.home.HomeScreenDataSource
import com.example.blogappdemo.databinding.FragmentCameraBinding
import com.example.blogappdemo.domain.camera.CameraRepoImpl
import com.example.blogappdemo.domain.home.HomeScreenRepoImpl
import com.example.blogappdemo.presentation.camera.CameraViewModel
import com.example.blogappdemo.presentation.camera.CameraViewModelFactory
import com.example.blogappdemo.presentation.home.HomeScreenViewModel
import com.example.blogappdemo.presentation.home.HomeScreenViewModelFactory
import com.example.blogappdemo.ui.home.adapter.HomeScreenAdapter

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var binding: FragmentCameraBinding
    private var bitmap:Bitmap? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val viewModel by viewModels<CameraViewModel> {
        CameraViewModelFactory(CameraRepoImpl(CameraDataSource()))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            //en caso de no tener camara
            Toast.makeText(
                requireContext(),
                "No se encontro ninguna app para abrir la camara",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.btnUploadPhoto.setOnClickListener {
            bitmap?.let {
                viewModel.uploadPhoto(bitmap!!, binding.etxDescription.text.toString().trim()).observe(viewLifecycleOwner, Observer{
                    when(it){
                        is  Result.Loading -> {
                            Toast.makeText(
                                requireContext(),
                                "Uploanding photo...",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                        is Result.Success -> {
                            findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)

                        }
                        is Result.Failure -> {
                            Log.d("error","Ocurrio un error: ${it.exception}")

                        }
                    }
                })
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.postImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        }
    }
}