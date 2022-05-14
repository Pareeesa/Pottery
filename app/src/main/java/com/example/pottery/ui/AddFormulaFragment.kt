package com.example.pottery.ui

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pottery.R
import com.example.pottery.adapters.NewItemAdapter
import com.example.pottery.databinding.FragmentAddFormulaBinding
import com.example.pottery.room.Formula
import com.example.pottery.viewModels.FormulaViewModel
import com.example.pottery.viewModels.NewItem

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
        val adapter = NewItemAdapter {}
        viewModel.itemListLiveData.observe(viewLifecycleOwner) {
            if (it != null){
            binding.recyclerView.adapter = adapter
            adapter.submitList(it)
            }
        }
        binding.btnAddItem.setOnClickListener {
            if (hasEmptyField()) {
                checkForErrors()
                return@setOnClickListener
            }
            if (viewModel.isItemRepeated(NewItem(binding.etCode.text.toString(),binding.etMaterial.text.toString(),
                    binding.etAmount.text.toString().toInt()))
            ){
                Toast.makeText(requireContext(), R.string.`already_exist_ّItem`, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            addItem()
            Toast.makeText(requireContext(), "Item added!", Toast.LENGTH_SHORT).show()
            binding.apply {
                etAmount.setText("")
                etMaterial.setText("")
                etCode.setText("")
            }
        }
        binding.btnCreate.setOnClickListener {
            if (binding.etFormulaName.text.isNullOrEmpty()) {
                    setError(binding.etFormulaName)
                    return@setOnClickListener
                }

            if (!viewModel.isFormulaNew(binding.etFormulaName.text.toString())) {
                Toast.makeText(requireContext(), R.string.`already_exist_ّFormula`, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.insertFormula(Formula(0,binding.etFormulaName.text.toString()))
            viewModel.insertItems(binding.etFormulaName.text.toString())
            findNavController().navigate(R.id.action_addFormulaFragment_to_homeFragment)
            Toast.makeText(requireContext(),R.string.successfully_saved, Toast.LENGTH_SHORT).show()
            }
        }
    private fun hasEmptyField(): Boolean {
        return (binding.etCode.text.isNullOrEmpty() || binding.etMaterial.text.isNullOrEmpty() ||
                binding.etAmount.text.isNullOrEmpty())
    }

    private fun checkForErrors() {
        setError(binding.etCode)
        setError(binding.etMaterial)
        setError(binding.etAmount)
    }

    private fun setError(editText: EditText) {
        if (editText.text.isNullOrEmpty())
            editText.error = "Must be filled"
    }

    private fun addItem() {
        val item = NewItem(binding.etCode.text.toString(),
            binding.etMaterial.text.toString(),
            binding.etAmount.text.toString().toInt())
        if (!viewModel.isItemRepeated(item)){
            viewModel.addItemToList(item)
            return
        }
    }

}