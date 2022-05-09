package com.example.pottery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pottery.R
import com.example.pottery.databinding.FormulaItemViewBinding
import com.example.pottery.room.Formula
typealias ClickHandler = (Formula) -> Unit
typealias ClickHandlerDelete = (Formula) -> Unit
typealias ClickHandleEdit = (Formula) -> Unit
class FormulaAdapter(private val clickHandler: ClickHandler,private val clickHandlerD: ClickHandlerDelete):
    ListAdapter<Formula, FormulaAdapter.ItemHolder>(FormulaDiffCallBack) {
    object FormulaDiffCallBack: DiffUtil.ItemCallback<Formula>() {
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
        holder.binding.imageViewDelete.setOnClickListener {
            clickHandlerD.invoke(getItem(position))
        }
        holder.binding.imageViewEdit.setOnClickListener {

        }
        holder.binding.cv.setOnClickListener {
            clickHandler.invoke(getItem(position))
        }
    }
}
