package com.example.pottery.ui


import android.app.Activity.RESULT_OK
import android.content.Intent
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


class NavigateFragment : Fragment() {
    private lateinit var binding: FragmentNavigateBinding
    val formulaViewModel: FormulaViewModel by activityViewModels()
    private val dir = Environment.getExternalStorageDirectory().toString() + "/Download"
    lateinit var filePath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavigateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    fun getBackup() {
        Backup.Init()
            .database(FormulaDataBase.getDatabase(requireContext()))
            .path(dir)
            .fileName("TooskaWoodBackupFile.txt")
            .onWorkFinishListener { success, message ->
                Toast.makeText(
                    requireContext(),
                    "تهیه نسخه پشنتیبان موفقیت آمیز بود، این نسخه در پوشه دانلود در فایل ها قابل مشاهده و بازیابی است",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .execute()
    }


    fun restoreBackup() {
        Restore.Init()
            .database(FormulaDataBase.getDatabase(requireContext()))
            .backupFilePath(filePath)
            .onWorkFinishListener { success, message ->
                Toast.makeText(
                    requireContext(),
                    "اطلاعات با موفقیت بازیابی شد ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .execute()
    }

    fun openDialog() {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            val file = File(uri?.path)
            val split: List<String> = file.getPath().split(":")
            filePath = split[1]
            if (filePath.contains(".txt"))
                restoreBackup()
            else
                Toast.makeText(
                    requireContext(),
                    "پسوند فایل قایل قبول نیست! ",
                    Toast.LENGTH_SHORT
                ).show()


        }
    }


}