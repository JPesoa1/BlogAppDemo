package com.example.blogappdemo.domain.home

import com.example.blogappdemo.core.Result
import com.example.blogappdemo.data.model.Post
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepo {
    suspend fun getLatestPosts(): Flow<Result<List<Post>>> //Dentro del sealed class
}