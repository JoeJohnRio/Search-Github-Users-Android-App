package com.example.githubsearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SearchAdapter(private val mCtx: Context) : ListAdapter<SearchItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(mCtx).inflate(R.layout.image_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val response = getItem(position) as SearchItem
        holder as ItemViewHolder
        holder.bindItemSearch(response)

    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var login: TextView
        var avatarUrl: ImageView

        init {
            avatarUrl = itemView.findViewById(R.id.user_image)
            login = itemView.findViewById(R.id.user)
        }
        fun bindItemSearch(response: SearchItem){

            login.text = response.login

            if (!response.avatarUrl?.isEmpty()!!) {
                Picasso.get()
                    .load(response.avatarUrl)
                    .fit()
                    .into(avatarUrl)
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}