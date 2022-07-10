package com.example.pottery.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.test.core.app.ApplicationProvider
import com.example.pottery.R
import com.example.pottery.adapters.ItemAdapter
import com.example.pottery.databinding.FragmentEditBinding
import com.example.pottery.room.Formula
import com.example.pottery.room.Item
import com.example.pottery.viewModels.EditViewModel
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private val viewModel: EditViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()
    private lateinit var adapter :ItemAdapter
    private lateinit var currentPhotoPath: String

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
        formula?.observe(viewLifecycleOwner){ formula1 ->
            if (formula1 != null){
                binding.etFormulaName.setText(formula1.formula.formulaName)
                currentPhotoPath = formula1.formula.imagePath
                val files = requireContext().filesDir.listFiles()
                files?.filter { it.canRead() && it.isFile  && it.name=="${currentPhotoPath}.jpg"  }
                    ?.map {
                        val bytes = it.readBytes()
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        binding.ivPicture.setImageBitmap(bmp)
                    }
                adapter = ItemAdapter(
                    {item ->
                    viewModel.deleteItem(item)
                },{ item ->
                        val action = EditFragmentDirections.actionEditFragmentToAddEditItemFragment(item)
                        findNavController().navigate(action)
                })
                binding.recyclerView.adapter = adapter
                adapter.submitList(formula1.items)
            }
        }
        val chooseImage = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                savePhotoToInternalStorage(it)
                binding.ivPicture.setImageBitmap(it)
            }
        }
        binding.btnEditImage.setOnClickListener {
            if(checkAndRequestPermissions())
                chooseImage.launch()
        }
        binding.btnSave.setOnClickListener {
            if (binding.etFormulaName.text.isNullOrBlank()){
                Toast.makeText(requireContext(),R.string.must_be_filled, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updatedFormula = formula?.value?.formula?.let { it1 -> Formula(it1.id,binding.etFormulaName.text.toString(),
                currentPhotoPath) }
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
            val updatedFormula = formula?.value?.formula?.let { it1 -> Formula(it1.id,binding.etFormulaName.text.toString(),formula.value!!.formula.imagePath) }
            if (updatedFormula != null) {
                viewModel.update(updatedFormula)
                viewModel.updateItems(adapter.currentList,binding.etFormulaName.text.toString())
            }
            val action = EditFragmentDirections.actionEditFragmentToAddEditItemFragment(Item(0,"",
                binding.etFormulaName.text.toString(),"",0.0))
            findNavController().navigate(action)
        }
    }
    private fun savePhotoToInternalStorage(bmp:Bitmap):Boolean{
        return try {
            if (currentPhotoPath != "0" && !currentPhotoPath.startsWith("/storage")){
            context?.openFileOutput("$currentPhotoPath.jpg", Activity.MODE_PRIVATE).use { stream->
                if (!bmp.compress(Bitmap.CompressFormat.JPEG,95,stream))
                    throw IOException("COULDN'T SAVE BITMAP")
            }
            } else{
                val name = UUID.randomUUID().toString()
                currentPhotoPath = name
                context?.openFileOutput("$name.jpg", Activity.MODE_PRIVATE).use { stream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream))
                        throw IOException("COULDN'T SAVE BITMAP")
                }
            }
            true
        }catch (e: IOException){
            e.printStackTrace()
            false
        }
    }
    private fun checkAndRequestPermissions(): Boolean {
        val wExtstorePermission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (wExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        ApplicationProvider.getApplicationContext(),
                        "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT
                    )
                        .show()
                }
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        ApplicationProvider.getApplicationContext(),
                        "FlagUp Requires Access to Your Storage.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val chooseImage = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                        if (it != null) {
                            savePhotoToInternalStorage(it)
                            binding.ivPicture.setImageBitmap(it)
                        }
                    }
                    chooseImage.launch()
                }
            }
        }
    }

}
