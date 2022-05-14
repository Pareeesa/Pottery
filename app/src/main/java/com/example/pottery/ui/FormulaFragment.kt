package com.example.pottery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.pottery.adapters.ItemAdapter
import com.example.pottery.adapters.nameOfFormula
import com.example.pottery.databinding.FragmentFormulaBinding
import com.example.pottery.room.Item
import com.example.pottery.viewModels.FormulaViewModel

class FormulaFragment : Fragment() {
    private lateinit var binding: FragmentFormulaBinding
    private val viewModel: FormulaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormulaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewFormulaName.text = nameOfFormula
        val adapterItems = ItemAdapter()
        var itemsList: List<Item>? = listOf()
        var total = 0
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

        }

    }
}