package com.example.pottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pottery.R
import com.example.pottery.databinding.FragmentAddEditItemBinding
import com.example.pottery.room.Item
import com.example.pottery.viewModels.FormulaViewModel

class AddEditItemFragment : Fragment() {

    private lateinit var binding: FragmentAddEditItemBinding
    private val viewModel: FormulaViewModel by viewModels()
    private val args: AddEditItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Tooska Wood"
        var isNew = true
        val item = args.item
        if (item.code != "") {
            isNew = false
            binding.apply {
                etCode.setText("" + item.code)
                etMaterial.setText("" + item.material)
                etAmount.setText(("" + item.amount))
            }
        }
        binding.btnSaveItem.setOnClickListener {
            if (hasEmptyField()) {
                if (hasEmptyField()) {
                    checkForErrors()
                    return@setOnClickListener
                }
            }
            if (isNew) {
                if (viewModel.itemIsRepeated(binding.etCode.text.toString(), item.formulaName)) {
                    Toast.makeText(
                        requireContext(),
                        R.string.`already_exist_ّItem`,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    viewModel.addItem(
                        Item(
                            0,
                            binding.etCode.text.toString(),
                            item.formulaName,
                            binding.etMaterial.text.toString(),
                            getAmount()
                        )
                    )
                    val action =
                        AddEditItemFragmentDirections.actionAddEditItemFragmentToEditFragment(item.formulaName)
                    findNavController().navigate(action)
                    Toast.makeText(requireContext(), R.string.item_added, Toast.LENGTH_SHORT).show()
                }
            }else {
                if (viewModel.itemIsRepeated(item.code, item.formulaName)) {
                    Toast.makeText(
                        requireContext(),
                        R.string.`already_exist_ّItem`,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    viewModel.updateItem(
                        Item(
                            item.id,
                            binding.etCode.text.toString(),
                            item.formulaName,
                            binding.etMaterial.text.toString(),
                            getAmount()
                        )
                    )
                    val action =
                        AddEditItemFragmentDirections.actionAddEditItemFragmentToEditFragment(item.formulaName)
                    findNavController().navigate(action)
                    Toast.makeText(requireContext(), R.string.item_changed, Toast.LENGTH_SHORT)
                        .show()
                }
            }

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
            editText.error = resources.getString(R.string.must_be_filled)
    }

    private fun isDouble(userInput: String): Boolean {
        return userInput.contains(".")
    }

    private fun getAmount(): Double {
        return if (!isDouble(binding.etAmount.text.toString()))
            binding.etAmount.text.toString().toInt().toDouble()
        else
            binding.etAmount.text.toString().toDouble()
    }
}