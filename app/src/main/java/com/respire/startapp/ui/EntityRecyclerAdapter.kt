package com.respire.startapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.respire.startapp.R
import com.respire.startapp.database.Entity
import com.respire.startapp.databinding.ItemRecyclerEntityBinding

class EntityRecyclerAdapter(var data: List<Entity>, var itemClick : (marketId : String?) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val binding = DataBindingUtil.inflate<ItemRecyclerEntityBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_entity,
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

    inner class EntityHolder(var binding: ItemRecyclerEntityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun showEntity(entity: Entity) {
            binding.viewModel = entity
            binding.executePendingBindings()
            binding.container.setOnClickListener {
                itemClick(data[adapterPosition].marketId)
            }
        }
    }
}