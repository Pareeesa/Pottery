package com.example.pottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pottery.R
import com.example.pottery.databinding.FragmentCalculatorBinding
import com.example.pottery.databinding.FragmentHomeBinding
import com.example.pottery.viewModels.CalculatorViewModel


class CalculatorFragment : Fragment() {
    private lateinit var binding: FragmentCalculatorBinding
    private var mCalculatorViewModel: CalculatorViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCalculatorViewModel = ViewModelProviders.of(this).get(CalculatorViewModel::class.java)

        mCalculatorViewModel.apply {
            this!!.getInvalidExpressionMessageEvent().observe(viewLifecycleOwner
            ) { shouldShow ->
                if (shouldShow != null && shouldShow) {
                    showInvalidExpressionMessage()
                }
            }
        }

        binding.let {
            it.viewModel = mCalculatorViewModel
            it.lifecycleOwner = this
        }
    }

    private fun showInvalidExpressionMessage() {
        Toast.makeText(
            requireContext(),
            getString(R.string.invalid_expression_message),
            Toast.LENGTH_SHORT
        ).show()
    }
}