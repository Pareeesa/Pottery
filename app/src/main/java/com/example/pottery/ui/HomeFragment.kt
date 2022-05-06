package com.example.pottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pottery.R
import com.example.pottery.adapters.FormulaAdapter
import com.example.pottery.databinding.FragmentHomeBinding
import com.example.pottery.viewModels.FormulaViewModel

class HomeFragment : Fragment() {

    private lateinit var binding :FragmentHomeBinding
    private val viewModel: FormulaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.formulaList?.observe(viewLifecycleOwner) { it ->
            if (it != null) {
                val adapter = FormulaAdapter{
                   findNavController().navigate(R.id.action_homeFragment_to_formulaFragment)
                }
                binding.recyclerView.adapter = adapter
                adapter.submitList(it)
            }
        }
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFormulaFragment)
        }
    }
}