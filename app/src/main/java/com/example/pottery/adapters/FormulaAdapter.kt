package com.example.pottery.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.pottery.R
import com.example.pottery.databinding.FormulaItemViewBinding
import com.example.pottery.room.Formula
import java.io.File


typealias ClickHandler = (Formula) -> Unit
typealias ClickHandlerDelete = (Formula) -> Unit
typealias ClickHandleEdit = (Formula) -> Unit
var nameOfFormula=""
class FormulaAdapter(private val clickHandler: ClickHandler,private val clickHandlerD: ClickHandlerDelete,private val clickHandlerE: ClickHandleEdit):
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
            clickHandlerE.invoke(getItem(position))
        }
        holder.binding.cv.setOnClickListener {
            nameOfFormula= getItem(position).formulaName
            clickHandler.invoke(getItem(position))
        }
        val imgFile = File(getItem(position).imagePath)
        if (imgFile.exists()) {
            val requestOptions = RequestOptions()
            Glide.with(holder.binding.imageView.context).load(BitmapFactory.decodeFile(imgFile.absolutePath))
                .apply(requestOptions.transforms(CenterCrop(), RoundedCorners(16)))
                .into(holder.binding.imageView)
        }
    }
}
