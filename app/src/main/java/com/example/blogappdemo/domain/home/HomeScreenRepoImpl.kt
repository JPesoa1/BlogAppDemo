package com.example.blogappdemo.domain.home

import com.example.blogappdemo.core.Result
import com.example.blogappdemo.data.model.Post
import com.example.blogappdemo.data.remote.home.HomeScreenDataSource
import kotlinx.coroutines.flow.Flow

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource): HomeScreenRepo {
    override suspend fun getLatestPosts(): Flow<Result<List<Post>>> = dataSource.getLatesPosts()
}