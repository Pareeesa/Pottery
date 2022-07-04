package com.example.pottery.ui

import android.Manifest
import android.app.Activity
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.pottery.R
import com.example.pottery.adapters.ItemAdapter
import com.example.pottery.databinding.FragmentEditBinding
import com.example.pottery.room.Formula
import com.example.pottery.room.Item
import com.example.pottery.viewModels.EditViewModel
import java.io.ByteArrayOutputStream

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
        formula?.observe(viewLifecycleOwner){
            if (it != null){
                binding.etFormulaName.setText(it.formula.formulaName)
                currentPhotoPath = it.formula.imagePath
                val requestOptions = RequestOptions()
                Glide.with(binding.ivPicture.context).load(BitmapFactory.decodeFile(it.formula.imagePath))
                    .apply(requestOptions.transforms(CenterCrop(), RoundedCorners(16)))
                    .into(binding.ivPicture)
                adapter = ItemAdapter(
                    {item ->
                    viewModel.deleteItem(item)
                },{ item ->
                        val action = EditFragmentDirections.actionEditFragmentToAddEditItemFragment(item)
                        findNavController().navigate(action)
                })
                binding.recyclerView.adapter = adapter
                adapter.submitList(it.items)
            }
        }
        binding.btnEditImage.setOnClickListener {
            if(checkAndRequestPermissions())
                chooseImage()
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
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && data != null) {
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
                1 -> if (resultCode == Activity.RESULT_OK && data != null) {
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
