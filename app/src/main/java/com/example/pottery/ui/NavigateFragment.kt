package com.example.pottery.ui

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pottery.R
import com.example.pottery.databinding.FragmentNavigateBinding
import com.example.pottery.room.FormulaDataBase
import com.example.pottery.viewModels.FormulaViewModel
import ir.androidexception.roomdatabasebackupandrestore.Backup
import ir.androidexception.roomdatabasebackupandrestore.Restore


class NavigateFragment : Fragment() {
    private lateinit var binding: FragmentNavigateBinding
    val formulaViewModel: FormulaViewModel by activityViewModels()
    private val folderMain = "Download"

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
            val dir = getDir()
            Backup.Init()
                .database(FormulaDataBase.getDatabase(requireContext()))
                .path(dir)
                .fileName("TooskaWoodBackupFile.txt")
                .onWorkFinishListener { success, message ->
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                }
                .execute()
        }

        binding.button2.setOnClickListener {
            val dir = getDir()
            Restore.Init()
                .database(FormulaDataBase.getDatabase(requireContext()))
                .backupFilePath("$dir/TooskaWoodBackupFile.txt")
                .onWorkFinishListener { success, message ->
                    Toast.makeText(requireContext(), "success restore ", Toast.LENGTH_SHORT).show()
                }
                .execute()
        }
    }

    fun getDir(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()
        } else
            return Environment.getExternalStorageDirectory().toString()

    }
}