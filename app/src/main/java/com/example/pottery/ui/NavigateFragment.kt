package com.example.pottery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pottery.R
import com.example.pottery.databinding.FragmentAddFormulaBinding
import com.example.pottery.databinding.FragmentFormulaBinding
import com.example.pottery.databinding.FragmentNavigateBinding


class NavigateFragment : Fragment() {
    private lateinit var binding: FragmentNavigateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavigateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardVieww.visibility= View.GONE
        binding.cardViewInsert.setOnClickListener {
            findNavController().navigate(R.id.action_navigateFragment_to_addFormulaFragment)
        }

        binding.cardViewSearch.setOnClickListener {
            findNavController().navigate(R.id.action_navigateFragment_to_homeFragment)
        }
    }
}