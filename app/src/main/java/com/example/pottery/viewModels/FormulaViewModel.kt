package com.example.pottery.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pottery.room.Formula
import com.example.pottery.room.FormulaRepository
import com.example.pottery.room.Item

class FormulaViewModel(app: Application) : AndroidViewModel(app) {
    private var repository = FormulaRepository()
    var formulaList: LiveData<List<Formula>>

    init {
        repository.initDB(app.applicationContext)
        formulaList = repository.formulaList
    }

    fun addFormula(formula: Formula) {
        repository.addFormula(formula)
    }
}

