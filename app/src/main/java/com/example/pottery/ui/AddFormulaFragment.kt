package com.example.pottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pottery.databinding.FragmentAddFormulaBinding
import com.example.pottery.room.Formula
import com.example.pottery.room.Item
import com.example.pottery.viewModels.FormulaViewModel

class AddFormulaFragment : Fragment() {

    private lateinit var binding: FragmentAddFormulaBinding
    private val viewModel: FormulaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFormulaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddItem.setOnClickListener {
            if (hasEmptyField()) {
                checkForErrors()
                return@setOnClickListener
            }
            val formula =viewModel.findFormula(binding.etFormulaId.text.toString().toInt())
            val item = Item(
                binding.etId.text.toString().toInt(),
                binding.etMaterial.text.toString(),
                binding.etAmount.text.toString().toDouble()
            )
            if (formula != null){
                viewModel.update(Formula(formula.id,formula.itemList.plus(item)))
            }else
                viewModel.insert(Formula(0, arrayListOf(item)))
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