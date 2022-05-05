package com.example.pottery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pottery.R
import com.example.pottery.databinding.FormulaItemViewBinding
import com.example.pottery.room.Formula
import java.util.logging.Handler

class FormulaAdapter(private val clickHandler: Handler):
    ListAdapter<Formula, FormulaAdapter.ItemHolder>(WordDiffCallBack) {
    object WordDiffCallBack: DiffUtil.ItemCallback<Formula>() {
        override fun areItemsTheSame(oldItem: Formula, newItem: Formula): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Formula, newItem: Formula): Boolean {
            return oldItem.id == newItem.id
        }
    }


    class ItemHolder(val binding: FormulaItemViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: FormulaItemViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.formula_item_view,
            parent,
            false
        )
        return ItemHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.formula = getItem(position)
//        holder.binding.ll.setOnClickListener {
//            clickHandler.invoke(getItem(position))
//        }
    }
}
