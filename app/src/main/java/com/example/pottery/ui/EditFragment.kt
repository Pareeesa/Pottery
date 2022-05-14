package com.example.pottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pottery.R
import com.example.pottery.adapters.ItemAdapter
import com.example.pottery.databinding.FragmentEditBinding
import com.example.pottery.room.Formula
import com.example.pottery.viewModels.FormulaViewModel

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private val viewModel: FormulaViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val x = viewModel.findFormulaByName(args.formulaName)
        x?.observe(viewLifecycleOwner){
            if (it != null){
                binding.etNameFormula.setText(it.formula.formulaName)
                val adapter = ItemAdapter {item ->
                    viewModel.deleteItem(item)
                }
                binding.recyclerView.adapter = adapter
                adapter.submitList(it.items)
            }
        }

        binding.btnSave.setOnClickListener {
            val updatedFormula = x?.value?.formula?.let { it1 -> Formula(it1.id,binding.etNameFormula.text.toString()) }
            if (updatedFormula != null) {
                viewModel.update(updatedFormula)
            }
            findNavController().navigate(R.id.action_editFragment_to_homeFragment)
        }
    }
}
