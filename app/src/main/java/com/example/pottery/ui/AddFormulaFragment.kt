package com.example.pottery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pottery.R
import com.example.pottery.databinding.FragmentAddFormulaBinding
import com.example.pottery.databinding.FragmentHomeBinding
import com.example.pottery.room.Formula
import com.example.pottery.room.Item
import com.example.pottery.viewModels.FormulaViewModel
import java.util.*

class AddFormulaFragment : Fragment() {

    private lateinit var binding: FragmentAddFormulaBinding
    //private val viewModel: FormulaViewModel by viewModels()
    val args:AddFormulaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFormulaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvId.text = args.id.toString()
//        binding.btnAddItem.setOnClickListener {
//            if (hasEmptyField()) {
//                checkForErrors()
//                return@setOnClickListener
//            }
//            var lastFormulaItemList = viewModel.getLast()?.itemList
//            lastFormulaItemList?.add(Item(binding.etId.text.toString().toInt(),binding.etMaterial.text.toString(),
//                binding.etAmount.text.toString().toDouble()))
//            lastFormulaItemList?.let { it1 -> Formula(args.id, it1) }?.let { it2 ->
//                viewModel.update(
//                    it2
//                )
//            }
//        }
    }
    private fun hasEmptyField(): Boolean {
        return (binding.etId.text.isNullOrEmpty() || binding.etMaterial.text.isNullOrEmpty() ||
                binding.etAmount.text.isNullOrEmpty())
    }

    private fun checkForErrors() {
        setError(binding.etId)
        setError(binding.etMaterial)
        setError(binding.etAmount)
    }

    private fun setError(editText: EditText) {
        if (editText.text.isNullOrEmpty())
            editText.error = "Must be filled"
    }
}