package com.example.pottery.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pottery.room.Formula
import com.example.pottery.room.FormulaRepository

class FormulaViewModel(app: Application) : AndroidViewModel(app)  {
    private var repository = FormulaRepository()

    init {
        repository.initDB(app.applicationContext)
    }

    fun addWord(formula:Formula) {
        repository.addFormula(formula)
    }
}

