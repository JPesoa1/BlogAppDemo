package com.example.blogappdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.blogappdemo.R
import com.example.blogappdemo.core.hide
import com.example.blogappdemo.core.show
import com.example.blogappdemo.databinding.ActivityMainBinding
import com.google.firebase.firestore.core.View

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        observeDestinationChange()




    }
    private fun observeDestinationChange(){
        navController.addOnDestinationChangedListener{controller,destination,arguments ->

            when(destination.id){
                R.id.loginFragment->{
                    binding.bottomNavigationView.hide()
                }
                R.id.homeScreenFragment->{
                    binding.bottomNavigationView.show()
                }
                R.id.registerFragment->{
                    binding.bottomNavigationView.hide()
                }
                R.id.cameraFragment->{
                    binding.bottomNavigationView.show()
                }
                R.id.profileFragment->{
                    binding.bottomNavigationView.show()
                }
                else->{
                    binding.bottomNavigationView.hide()
                }

            }
        }
    }
}