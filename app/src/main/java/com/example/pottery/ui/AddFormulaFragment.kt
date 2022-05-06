package com.example.pottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pottery.adapters.ItemAdapter
import com.example.pottery.databinding.FragmentAddFormulaBinding
import com.example.pottery.room.Formula
import com.example.pottery.room.Item
import com.example.pottery.viewModels.FormulaViewModel

class AddFormulaFragment : Fragment() {

    private lateinit var binding: FragmentAddFormulaBinding
    private val viewModel: FormulaViewModel by viewModels()
    val list : MutableList<Item> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFormulaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemAdapter()
        viewModel.itemListLiveData.observe(viewLifecycleOwner){
            binding.recyclerView.adapter = adapter
            adapter.submitList(it)
        }

        binding.btnAddItem.setOnClickListener {
            if (hasEmptyField()) {
                checkForErrors()
                return@setOnClickListener
            }
            viewModel.addItem(
                Item(
                binding.etId.text.toString().toInt(),
                binding.etMaterial.text.toString(),
                binding.etAmount.text.toString().toDouble())
            )
        }
        binding.btnSave.setOnClickListener {
            val formula =viewModel.findFormula(binding.etFormulaId.text.toString().toInt())
            if (formula != null){
                viewModel.update(Formula(formula.id,formula.itemList.plus(list)))
            }else
                viewModel.insert(Formula(0, list))
        }
    }
    private fun hasEmptyField(): Boolean {
        return (binding.etFormulaId.text.isNullOrEmpty() ||binding.etId.text.isNullOrEmpty() || binding.etMaterial.text.isNullOrEmpty() ||
                binding.etAmount.text.isNullOrEmpty())
    }

    private fun checkForErrors() {
        setError(binding.etId)
        setError(binding.etMaterial)
        setError(binding.etAmount)
        setError(binding.etFormulaId)
    }

    private fun setError(editText: EditText) {
        if (editText.text.isNullOrEmpty())
            editText.error = "Must be filled"
    }
}