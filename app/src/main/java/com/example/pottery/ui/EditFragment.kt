package com.example.pottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pottery.R
import com.example.pottery.adapters.ItemAdapter
import com.example.pottery.databinding.FragmentEditBinding
import com.example.pottery.room.Formula
import com.example.pottery.room.Item
import com.example.pottery.viewModels.FormulaViewModel

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private val viewModel: EditViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()
    lateinit var adapter :ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val formula = viewModel.findFormulaByName(args.formulaName)
        activity?.title = args.formulaName
        formula?.observe(viewLifecycleOwner){
            if (it != null){
                binding.etFormulaName.setText(it.formula.formulaName)
                adapter = ItemAdapter(
                    {item ->
                    viewModel.deleteItem(item)
                },{ item ->
                        val action = EditFragmentDirections.actionEditFragmentToAddEditItemFragment(item)
                        findNavController().navigate(action)
                })
                binding.recyclerView.adapter = adapter
                adapter.submitList(it.items)
            }
        }
        binding.btnSave.setOnClickListener {
            if (binding.etFormulaName.text.isNullOrBlank()){
                Toast.makeText(requireContext(),R.string.must_be_filled, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updatedFormula = formula?.value?.formula?.let { it1 -> Formula(it1.id,binding.etFormulaName.text.toString()) }
            if (updatedFormula != null) {
                viewModel.update(updatedFormula)
                viewModel.updateItems(adapter.currentList,binding.etFormulaName.text.toString())
            }
            findNavController().navigate(R.id.action_editFragment_to_homeFragment)
        }

        binding.btnAddItem.setOnClickListener {
            if (binding.etFormulaName.text.isNullOrBlank()){
                binding.etFormulaName.error = resources.getString(R.string.must_be_filled)
                return@setOnClickListener
            }
            val updatedFormula = formula?.value?.formula?.let { it1 -> Formula(it1.id,binding.etFormulaName.text.toString()) }
            if (updatedFormula != null) {
                viewModel.update(updatedFormula)
                viewModel.updateItems(adapter.currentList,binding.etFormulaName.text.toString())
            }
            val action = EditFragmentDirections.actionEditFragmentToAddEditItemFragment(Item(0,"",
                binding.etFormulaName.text.toString(),"",0.0))
            findNavController().navigate(action)
        }
    }
}
