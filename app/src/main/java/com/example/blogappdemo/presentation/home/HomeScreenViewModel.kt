package com.example.blogappdemo.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogappdemo.core.Result
import com.example.blogappdemo.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class HomeScreenViewModel(private val repo: HomeScreenRepo):ViewModel() {
    fun fetchLatesPosts() = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        kotlin.runCatching {
            repo.getLatestPosts()
        }.onSuccess { postList->
           emit(postList)
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }
}

//Un viewmodel no puede tener un constructor por lo tanto usamo factory

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}
