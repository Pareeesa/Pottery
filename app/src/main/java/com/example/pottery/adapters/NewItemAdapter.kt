package com.example.pottery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pottery.R
import com.example.pottery.databinding.ItemViewBinding
import com.example.pottery.databinding.NewItemViewBinding
import com.example.pottery.room.Item
import com.example.pottery.viewModels.NewItem

typealias NewItemClickHandler = (NewItem) -> Unit

class NewItemAdapter(private val clickHandler: NewItemClickHandler): ListAdapter<NewItem, NewItemAdapter.ItemHolder>(ItemDiffCallBack) {

    object ItemDiffCallBack: DiffUtil.ItemCallback<NewItem>() {
        override fun areItemsTheSame(oldItem: NewItem, newItem: NewItem): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: NewItem, newItem: NewItem): Boolean {
            return oldItem.code == newItem.code
        }
    }
    class ItemHolder(val binding: NewItemViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: NewItemViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.new_item_view,
            parent,
            false
        )
        return ItemHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.item = getItem(position)
        holder.binding.btnDelete.setOnClickListener {
            clickHandler.invoke(getItem(position))
        }
    }
}
