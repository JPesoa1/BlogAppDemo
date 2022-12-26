package com.example.blogappdemo.ui.auth

import android.os.Bundle

import androidx.fragment.app.Fragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.blogappdemo.R
import com.example.blogappdemo.core.Result
import com.example.blogappdemo.data.remote.auth.AuthDataSource

import com.example.blogappdemo.databinding.FragmentRegisterBinding
import com.example.blogappdemo.domain.auth.AuthRepoImpl
import com.example.blogappdemo.presentation.auth.AuthViewModel
import com.example.blogappdemo.presentation.auth.AuthViewModelFactory


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterBinding.bind(view)
        signUp()

    }

    private fun signUp() {


        binding.btnSignup.setOnClickListener {

            val username = binding.textInputLayoutUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()

            if(validateUserData(password, confirmPassword, username, email) ) return@setOnClickListener

            createUser(email,password,username)
        }
    }

    private fun createUser(email: String, password: String, username: String) {
        viewModel.singUp(email, password,username).observe(viewLifecycleOwner, Observer {
            when(it){
                is Result.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled=false
                }
                is Result.Success ->{
                    binding.progressBar.visibility=View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)


                }
                is Result.Failure->{
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignup.isEnabled=true
                    Toast.makeText(requireContext(),"Error ${it.exception}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun validateUserData(
        password: String,
        confirmPassword: String,
        username: String,
        email: String
    ) :Boolean{
        if (password != confirmPassword) {
            binding.editTextConfirmPassword.error = "Password does not match"
            binding.editTextPassword.error = "Password does not match"

            return true
        }
        if (username.isEmpty()) {
            binding.textInputLayoutUsername.error = "Username empty"
            return true
        }
        if (email.isEmpty()) {
            binding.editTextEmail.error = "Email empty"
            return true
        }
        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password empty"
            return true
        }
        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "Confirm Password empty"
            return true
        }
        return false
    }


}