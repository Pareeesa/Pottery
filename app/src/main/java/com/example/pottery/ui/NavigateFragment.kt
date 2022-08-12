package com.example.pottery.ui

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pottery.BuildConfig
import com.example.pottery.R
import com.example.pottery.databinding.FragmentNavigateBinding
import com.example.pottery.room.FormulaDataBase
import com.example.pottery.viewModels.FormulaViewModel
import ir.androidexception.roomdatabasebackupandrestore.Backup
import ir.androidexception.roomdatabasebackupandrestore.Restore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class NavigateFragment : Fragment() {
    private lateinit var binding: FragmentNavigateBinding
    private val formulaViewModel: FormulaViewModel by activityViewModels()
    private val dir = Environment.getExternalStorageDirectory().toString() + "/TooskaWoodBackUp"
    private lateinit var textFilePath: String
    private lateinit var imagesPath: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentNavigateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        formulaViewModel.getAllFormulas()
        binding.imageViewInsert.setOnClickListener {
            findNavController().navigate(R.id.action_navigateFragment_to_addFormulaFragment)
        }

        binding.imageViewSearch.setOnClickListener {
            findNavController().navigate(R.id.action_navigateFragment_to_homeFragment)
        }
        binding.imageViewProducts.setOnClickListener {
            formulaViewModel.webViewURL = "https://www.tooskawood.ir/shop/"
            findNavController().navigate(R.id.action_navigateFragment_to_webViewFragment)
        }
        binding.imageViewGuide.setOnClickListener {
            formulaViewModel.webViewURL = "https://www.tooskawood.ir/tooskawood_glaze/"
            findNavController().navigate(R.id.action_navigateFragment_to_webViewFragment)
        }
        binding.imageViewTrainingClasses.setOnClickListener {
            formulaViewModel.webViewURL = "https://www.tooskawood.ir/pottery-academic/"
            findNavController().navigate(R.id.action_navigateFragment_to_webViewFragment)
        }
        binding.imageViewWorkShop.setOnClickListener {
            formulaViewModel.webViewURL = "https://www.tooskawood.ir/workshop/"
            findNavController().navigate(R.id.action_navigateFragment_to_webViewFragment)
        }
        binding.imageViewCalculator.setOnClickListener {
            findNavController().navigate(R.id.action_navigateFragment_to_calculatorFragment)
        }
        binding.imageViewAboutMe.setOnClickListener {
            findNavController().navigate(R.id.action_navigateFragment_to_aboutFragment)
        }
        binding.imageViewBook.setOnClickListener {
            formulaViewModel.webViewURL = "https://www.tooskawood.ir/books/"
            findNavController().navigate(R.id.action_navigateFragment_to_webViewFragment)
        }
        binding.imageViewArtist.setOnClickListener {
            formulaViewModel.webViewURL = "https://www.tooskawood.ir/visual-artists-of-iran/"
            findNavController().navigate(R.id.action_navigateFragment_to_webViewFragment)
        }
        binding.imageViewShowPlace.setOnClickListener {
            formulaViewModel.webViewURL = "https://www.tooskawood.ir/event/"
            findNavController().navigate(R.id.action_navigateFragment_to_webViewFragment)
        }

        binding.buttonBackup.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                getAccessibility()
                Toast.makeText(
                    requireContext(),
                    "لطفا ابتدا دسترسی مربوطه را به نرم افزار بدهید و سپس دوباره نسخه پشتیبان تهیه کنید",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                getBackup()
            }
        }

        binding.button2.setOnClickListener {
            openDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun getAccessibility() {
        val uri: Uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
    }

    private fun getBackup() {
        Backup.Init()
            .database(FormulaDataBase.getDatabase(requireContext()))
            .path(dir)
            .fileName("TooskaWoodBackupFile.txt")
            .onWorkFinishListener { _, _ ->
                Toast.makeText(
                    requireContext(),
                    "تهیه نسخه پشنتیبان موفقیت آمیز بود، این نسخه در پوشه دانلود در فایل ها قابل مشاهده و بازیابی است",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .execute()
        val files = context?.filesDir?.listFiles()
        for (i in formulaViewModel.getAllFormulas()!!){
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") && it.name=="${i.imagePath}.jpg"  }
                ?.map {
                    val bytes = it.readBytes()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    saveImageToExternal(bmp,i.imagePath)
                }
        }
    }
    private fun saveImageToExternal(finalBitmap: Bitmap,imagePath:String) {
        val myDir = File("$dir/images")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val fName = "$imagePath.jpg"
        val file = File(myDir, fName)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun saveBackUpImagesToInternalStorage(){
        val file = File("$dir/$imagesPath")
        val images = file.listFiles()
        for (image in images!!){
            try {
                context?.openFileOutput("${image.name}.jpg", Activity.MODE_PRIVATE).use { stream->
                    if (!(image as Bitmap).compress(Bitmap.CompressFormat.JPEG,95,stream))
                        throw IOException("COULDN'T SAVE BITMAP")
                }
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }
    private fun restoreBackup() {
        Restore.Init()
            .database(FormulaDataBase.getDatabase(requireContext()))
            .backupFilePath(textFilePath)
            .onWorkFinishListener { _, _ ->
                Toast.makeText(
                    requireContext(),
                    "اطلاعات با موفقیت بازیابی شد ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .execute()
        saveBackUpImagesToInternalStorage()
    }

    private fun openDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9999 && resultCode == RESULT_OK) {
            val directory = data?.data?.let { DocumentFile.fromTreeUri(requireContext(), it) }
            val files = directory?.listFiles()
            val txtFile = files?.get(0)
            val images = files?.get(1)
            if (txtFile?.name == "TooskaWoodBackupFile.txt" && images?.name == "images"){
                textFilePath = txtFile.uri.path.toString().split(":")[2]
                imagesPath = images.uri.path.toString().split(":")[2]
                restoreBackup()
            }else{
                Toast.makeText(requireContext(), "فایل مورد نظر یافت نشد!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}