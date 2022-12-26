package com.example.blogappdemo.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.blogappdemo.R
import com.example.blogappdemo.core.Result
import com.example.blogappdemo.core.hide
import com.example.blogappdemo.core.show
import com.example.blogappdemo.data.remote.home.HomeScreenDataSource
import com.example.blogappdemo.databinding.FragmentHomeScreenBinding
import com.example.blogappdemo.domain.home.HomeScreenRepoImpl
import com.example.blogappdemo.presentation.home.HomeScreenViewModel
import com.example.blogappdemo.presentation.home.HomeScreenViewModelFactory
import com.example.blogappdemo.ui.home.adapter.HomeScreenAdapter



class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(HomeScreenRepoImpl(HomeScreenDataSource()))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)
        viewModel.fetchLatesPosts().observe(viewLifecycleOwner, Observer {
            when(it){
                is  Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    if(it.data.isEmpty()){
                        binding.emptyContainer.show()
                        return@Observer
                    }else{
                        binding.emptyContainer.hide()
                    }
                    binding.rvHome.adapter = HomeScreenAdapter(it.data)


                }
                is Result.Failure -> {
                    Log.d("error","Ocurrio un error: ${it.exception}")

                }
            }
        })


    }
}