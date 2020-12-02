package com.arts.amanda.ui.tabs.feed

import androidx.recyclerview.widget.RecyclerView
import com.arts.amanda.data.Arts
import com.arts.amanda.databinding.ActivityFeedHolderBinding
import com.arts.amanda.di.GlideApp

class FeedHolder(private val binding: ActivityFeedHolderBinding)
    : RecyclerView.ViewHolder(binding.root) {

        fun bind(arts: Arts) {
            binding.holderTitle.text = arts.title
            binding.dateHolder.text = arts.date
            GlideApp.with(this.itemView).load(arts.image).into(binding.holderImage)
            binding.holderDescription.text = arts.description
        }
}