package com.example.blogappdemo.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogappdemo.R
import com.example.blogappdemo.core.Result
import com.example.blogappdemo.data.remote.auth.AuthDataSource
import com.example.blogappdemo.data.remote.home.HomeScreenDataSource
import com.example.blogappdemo.databinding.FragmentSetupProfileBinding
import com.example.blogappdemo.domain.auth.AuthRepoImpl
import com.example.blogappdemo.domain.home.HomeScreenRepoImpl
import com.example.blogappdemo.presentation.auth.AuthViewModel
import com.example.blogappdemo.presentation.auth.AuthViewModelFactory
import com.example.blogappdemo.presentation.home.HomeScreenViewModel
import com.example.blogappdemo.presentation.home.HomeScreenViewModelFactory


class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {
    private lateinit var binding:FragmentSetupProfileBinding
    private val viewModel by viewModels<AuthViewModel> {
       AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }
    private var bitmap: Bitmap? =null
    private val REQUEST_IMAGE_CAPTURE=1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSetupProfileBinding.bind(view)
        binding.profileImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(),"No se encontro ninguna app para abrir la camara",
                    Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCreateProfile.setOnClickListener {
            val username = binding.textUsername.text.toString().trim()
            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Uploading photo....").create()
            bitmap?.let {
                if(username.isNotEmpty()){
                    viewModel.updateUserProfile(imageBitmap = it, username = username).observe(viewLifecycleOwner, Observer{
                        when(it){
                            is Result.Loading ->{
                                alertDialog.show()

                            }
                            is Result.Success ->{
                                alertDialog.dismiss()
                                findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                            }
                            is Result.Failure->{
                                Toast.makeText(requireContext(),"${it.exception}",Toast.LENGTH_LONG).show()

                            }
                        }
                    })

                }

            }


        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.profileImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        }
    }
}