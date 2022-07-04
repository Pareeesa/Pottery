package com.example.pottery.ui

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.pottery.R
import com.example.pottery.adapters.NewItemAdapter
import com.example.pottery.databinding.FragmentAddFormulaBinding
import com.example.pottery.room.Formula
import com.example.pottery.viewModels.FormulaViewModel
import com.example.pottery.viewModels.NewItem
import java.io.ByteArrayOutputStream


const val REQUEST_ID_MULTIPLE_PERMISSIONS=10
class AddFormulaFragment : Fragment() {

    private lateinit var binding: FragmentAddFormulaBinding
    private val viewModel: FormulaViewModel by viewModels()
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFormulaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPicture.setOnClickListener {
            if(checkAndRequestPermissions())
                chooseImage()
        }
        viewModel.itemListLiveData.observe(viewLifecycleOwner) {
            if (it != null){
                val adapter = NewItemAdapter { item ->
                    val updated = viewModel.itemListLiveData.value
                    updated?.remove(item)
                    viewModel.itemListLiveData.value = updated
                }
                binding.recyclerView.adapter = adapter
                adapter.submitList(it)
            }
        }
        binding.btnAddItem.setOnClickListener {
            if (hasEmptyField()) {
                checkForErrors()
                return@setOnClickListener
            }
            if (viewModel.isNewItemRepeated(NewItem(binding.etCode.text.toString(),binding.etMaterial.text.toString(),
                    getAmount()))
            ){
                Toast.makeText(requireContext(), R.string.`already_exist_ّItem`, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            addItem()
            Toast.makeText(requireContext(), R.string.item_added, Toast.LENGTH_SHORT).show()
            binding.apply {
                etAmount.setText("")
                etMaterial.setText("")
                etCode.setText("")
            }
        }
        binding.btnCreate.setOnClickListener {
            if (binding.etFormulaName.text.isNullOrEmpty()) {
                    setError(binding.etFormulaName)
                    return@setOnClickListener
                }

            if (!viewModel.isFormulaNew(binding.etFormulaName.text.toString())) {
                Toast.makeText(requireContext(), R.string.`already_exist_ّFormula`, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.insertFormula(Formula(0,binding.etFormulaName.text.toString(),currentPhotoPath))
            viewModel.insertItems(binding.etFormulaName.text.toString())
            findNavController().navigate(R.id.action_addFormulaFragment_to_homeFragment)
            Toast.makeText(requireContext(),R.string.successfully_saved, Toast.LENGTH_SHORT).show()
            }
        }
    private fun hasEmptyField(): Boolean {
        return (binding.etCode.text.isNullOrEmpty() || binding.etMaterial.text.isNullOrEmpty() ||
                binding.etAmount.text.isNullOrEmpty())
    }

    private fun checkForErrors() {
        setError(binding.etCode)
        setError(binding.etMaterial)
        setError(binding.etAmount)
    }

    private fun setError(editText: EditText) {
        if (editText.text.isNullOrEmpty())
            editText.error = resources.getString(R.string.must_be_filled)
    }

    private fun addItem() {
        val item = NewItem(binding.etCode.text.toString(),
            binding.etMaterial.text.toString(),
            getAmount())
        if (!viewModel.isNewItemRepeated(item)){
            viewModel.addItemToList(item)
            return
        }
    }
    private fun isDouble(userInput: String): Boolean {
        return userInput.contains(".")
    }
    private fun getAmount():Double{
        return if (!isDouble( binding.etAmount.text.toString()))
            binding.etAmount.text.toString().toInt().toDouble()
        else
            binding.etAmount.text.toString().toDouble()
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
                    chooseImage()
                }
            }
        }
    }
    private fun chooseImage() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
            .setNegativeButton("دوربین") { _, _ ->
                val photo = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(photo, 0)
            }
            .setPositiveButton("گالری") { _, _ ->
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            }
            .setNeutralButton("انصراف") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.extras!!["data"] as Bitmap?
                    val bytes = ByteArrayOutputStream()
                    selectedImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path: String = MediaStore.Images.Media.insertImage(
                        requireContext().contentResolver,
                        selectedImage,
                        "Title",
                        null
                    )
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor = requireContext().contentResolver.query(Uri.parse(path), filePathColumn, null, null, null)
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath: String = cursor.getString(columnIndex)
                            currentPhotoPath = picturePath
                            val requestOptions = RequestOptions()
                            Glide.with(binding.ivPicture.context).load(BitmapFactory.decodeFile(picturePath))
                                .apply(requestOptions.transforms(CenterCrop(), RoundedCorners(16)))
                                .into(binding.ivPicture)
                            cursor.close()
                        }
                    }
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage: Uri? = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor = requireContext().contentResolver.query(selectedImage, filePathColumn, null, null, null)
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath: String = cursor.getString(columnIndex)
                            currentPhotoPath = picturePath
                            val requestOptions = RequestOptions()
                            Glide.with(binding.ivPicture.context).load(BitmapFactory.decodeFile(picturePath))
                                .apply(requestOptions.transforms(CenterCrop(), RoundedCorners(16)))
                                .into(binding.ivPicture)
                            cursor.close()
                        }
                    }
                }
            }
        }
    }
}