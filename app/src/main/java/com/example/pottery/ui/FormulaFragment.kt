package com.example.pottery.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pottery.R
import com.example.pottery.adapters.DetailAdapter
import com.example.pottery.adapters.ItemConverterAdapter
import com.example.pottery.adapters.nameOfFormula
import com.example.pottery.databinding.FragmentFormulaBinding
import com.example.pottery.room.Item
import com.example.pottery.viewModels.FormulaViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt


class FormulaFragment : Fragment() {
    private lateinit var binding: FragmentFormulaBinding
    private val viewModel: FormulaViewModel by viewModels()
    private val adapterItems = ItemConverterAdapter()
    private var convertValue = 0.0
    private val convertedValueList = mutableListOf<Double>()
    private var itemsList: List<Item>? = listOf()
    var total = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormulaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.textViewFormulaName.text = nameOfFormula
        binding.recyclerViewItemsDetail.adapter = adapterItems
        viewModel.findFormulaByName(nameOfFormula)?.observe(viewLifecycleOwner)
        {
            adapterItems.submitList(it?.items)
            itemsList = adapterItems.currentList
            for (item in itemsList!!) {
                total += item.amount
            }
            val newTotal = (total * 10000).roundToInt().toDouble() / 10000
            binding.textViewTotal.text = newTotal.toString()
        }
        var adapterDetail = DetailAdapter(convertedValueList)
        binding.recyclerViewConvertValues.adapter = adapterDetail

        binding.buttonConvert.setOnClickListener {
            if (binding.editTextConvertValue.text.isNullOrBlank()) {
                binding.editTextConvertValue.error = resources.getString(R.string.must_be_filled)
                return@setOnClickListener
            }
            convertedValueList.clear()
            convertValue = binding.editTextConvertValue.text.toString().toDouble()
            for (item in itemsList!!) {
                val x = item.amount / total.toDouble() * convertValue
                val df = DecimalFormat("#.###")
                df.roundingMode = RoundingMode.DOWN
                val value = df.format(x)
                convertedValueList.add(value.toDouble())
            }
            adapterDetail = DetailAdapter(convertedValueList)
            binding.recyclerViewConvertValues.adapter = adapterDetail
            adapterDetail.notifyDataSetChanged()
        }


    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_menu_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    " نام فرمول:$nameOfFormula \n" + " \n"+
                            createStringForShare()
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
                return false
            }
        }
        return false
    }

    fun createStringForShare():String{
        var out = "مواد تشکیل دهنده : \n \n "
        for(item in itemsList!!){
           val thisItemString = " کد: ${item.code} \n ماده تشکیل دهنده: ${item.material} \n مقدار: ${item.amount} \n\n "
            out+=thisItemString
        }
        out+= "فرمول نویسی لعاب - تماشاخانه توسکاوود \n\n "
        out+= " http://tooskawood.ir "
        return out
    }
}