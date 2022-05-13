package com.example.pottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.pottery.adapters.ItemAdapter
import com.example.pottery.databinding.FragmentEditBinding
import com.example.pottery.viewModels.FormulaViewModel

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private val viewModel: FormulaViewModel by viewModels()
    private val args:EditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.findFormulaByName(args.formulaName)?.observe(viewLifecycleOwner){
            if (it != null){
                binding.etNameFormula.setText(it.formula.formulaName)
                val adapter = ItemAdapter()
                binding.recyclerView.adapter = adapter
                adapter.submitList(it.items)
            }
        }
    }
}
