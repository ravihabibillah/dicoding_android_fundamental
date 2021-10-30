package com.example.apigithubuserapp.setadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apigithubuserapp.ItemsItem
import com.example.apigithubuserapp.databinding.ItemUserBinding

class ListSearchUserAdapter(private val listUser: List<ItemsItem>) :
    RecyclerView.Adapter<ListSearchUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .circleCrop()
            .into(holder.ivAvatar)
        holder.tvUsername.text = listUser[position].login

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount() = listUser.size

    class ViewHolder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivAvatar: ImageView = binding.ivUser
        val tvUsername: TextView = binding.tvUsername
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}