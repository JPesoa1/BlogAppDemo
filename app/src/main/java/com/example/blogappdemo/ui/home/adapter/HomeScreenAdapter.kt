package com.example.blogappdemo.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogappdemo.core.BaseViewHolder
import com.example.blogappdemo.core.TimeUtils
import com.example.blogappdemo.core.hide
import com.example.blogappdemo.data.model.Post
import com.example.blogappdemo.databinding.PostItemViewBinding

class HomeScreenAdapter (private val  postList: List<Post>):RecyclerView.Adapter<BaseViewHolder<*>>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = PostItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeScreenViewHolder(itemBinding,parent.context)

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder){
            is HomeScreenViewHolder -> holder.bind(postList[position])
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    private inner class HomeScreenViewHolder(val binding:PostItemViewBinding,val context: Context):BaseViewHolder<Post>(binding.root){
        override fun bind(item: Post) {

            setupProfileInfo(item)
            addPostTimeStamp(item)
            setupPostImage(item)
            setupPostDescription(item)

        }

        private  fun setupProfileInfo(post:Post){
            Glide.with(context).load(post.profile_picture).centerCrop().into(binding.profilePicture)
            binding.profileName.text=post.profile_name
        }

        private  fun addPostTimeStamp(post:Post){
            val createdAt = (post.createdAt?.time?.div(1000L))?.let{
                TimeUtils.getTimeAgo(it.toInt())
            }
            binding.postTimestamp.text=createdAt
        }

        private fun setupPostImage(post:Post){
            Glide.with(context).load(post.post_image).centerCrop().into(binding.postImage)
        }

        private fun setupPostDescription(post:Post){

            if (post.post_description.isEmpty()){
                binding.postDescription.hide()
            }else{
                binding.postDescription.text=post.post_description
            }
        }
    }


}