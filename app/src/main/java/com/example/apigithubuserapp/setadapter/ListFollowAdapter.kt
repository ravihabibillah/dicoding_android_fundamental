package com.example.apigithubuserapp.setadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apigithubuserapp.FollowResponseItem
import com.example.apigithubuserapp.databinding.ItemUserBinding

class ListFollowAdapter(private val listFollow: List<FollowResponseItem>) :
    RecyclerView.Adapter<ListFollowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listFollow[position].avatarUrl)
            .circleCrop()
            .into(holder.ivAvatar)
        holder.tvUsername.text = listFollow[position].login
    }

    override fun getItemCount() = listFollow.size

    class ViewHolder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivAvatar: ImageView = binding.ivUser
        val tvUsername: TextView = binding.tvUsername
    }
}