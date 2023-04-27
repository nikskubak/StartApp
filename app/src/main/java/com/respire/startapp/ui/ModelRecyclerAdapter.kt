package com.respire.startapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.respire.startapp.databinding.ItemRecyclerEntityBinding
import com.respire.startapp.domain.models.Model

class ModelRecyclerAdapter(var data: List<Model>, var itemClick : (marketId : String?) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val binding = ItemRecyclerEntityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntityHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EntityHolder).showEntity(data[position])
    }

    inner class EntityHolder(val binding: ItemRecyclerEntityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun showEntity(entity: Model) {
            binding.container.setOnClickListener {
                itemClick(data[adapterPosition].marketId)
            }
            binding.name.text = entity.name
            binding.description.text = entity.description
            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            Glide.with(binding.logo)
                .load(entity.imageUrl)
                .apply(RequestOptions().circleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .placeholder(android.R.color.white)
                .into(binding.logo)
        }
    }
}