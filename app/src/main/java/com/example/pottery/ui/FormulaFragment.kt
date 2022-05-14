package com.example.pottery.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.pottery.adapters.DetailAdapter
import com.example.pottery.adapters.ItemAdapter
import com.example.pottery.adapters.nameOfFormula
import com.example.pottery.databinding.FragmentFormulaBinding
import com.example.pottery.room.Item
import com.example.pottery.viewModels.FormulaViewModel

class FormulaFragment : Fragment() {
    private lateinit var binding: FragmentFormulaBinding
    private val viewModel: FormulaViewModel by viewModels()
    val adapterItems = ItemAdapter()
    var convertValue = 0
    val convertedValueList = mutableListOf<Int>()
    var itemsList: List<Item>? = listOf()
    var total = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormulaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewFormulaName.text = nameOfFormula
        binding.recyclerViewItemsDetail.adapter = adapterItems
        viewModel.findFormulaByName(nameOfFormula)?.observe(viewLifecycleOwner)
        {
            adapterItems.submitList(it?.get(0)?.items)
            itemsList = adapterItems.currentList
            for (item in itemsList!!) {
                total += item.amount
            }
            binding.textViewTotal.text = total.toString()
        }

        binding.buttonConvert.setOnClickListener {
            convertValue = binding.editTextConvertValue.text.toString().toInt()
            for (item in itemsList!!) {
                convertedValueList.add(item.amount * total / convertValue)
            }
            val adapterDetail = DetailAdapter(convertedValueList)
            binding.recyclerViewConvertValues.adapter = adapterDetail
            adapterDetail.notifyDataSetChanged()
        }


    }
}