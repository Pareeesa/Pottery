package com.example.pottery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pottery.R
import com.example.pottery.adapters.FormulaAdapter
import com.example.pottery.databinding.FragmentHomeBinding
import com.example.pottery.room.Formula
import com.example.pottery.room.Item

class HomeFragment : Fragment() {

    private lateinit var binding :FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FormulaAdapter()
        binding.recyclerView.adapter = adapter
        val list = listOf(
            Formula(1, listOf(Item(1,"sotb",200.0))),
            Formula(2, listOf(Item(1,"sotb",200.0))),
            Formula(3, listOf(Item(1,"sotb",200.0)))
            )
        adapter.submitList(list)
    }
}