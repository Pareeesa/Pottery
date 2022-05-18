package com.example.pottery.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pottery.R
import com.example.pottery.databinding.FragmentAboutBinding
import com.example.pottery.databinding.FragmentAddFormulaBinding


class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewIconAbout.setOnClickListener{
            openURL()
        }
        binding.textViewURL.setOnClickListener{
            openURL()
        }
    }

    fun openURL()
    {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://tooskawood.ir"))
        startActivity(browserIntent)
    }
}