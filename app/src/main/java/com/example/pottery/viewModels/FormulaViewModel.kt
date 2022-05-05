package com.example.pottery.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pottery.room.Formula
import com.example.pottery.room.FormulaRepository

class FormulaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FormulaRepository = FormulaRepository(application)
    val formulaList: LiveData<List<Formula>?>? = repository.getAllFormula()
    /*init {
        insert(Formula(0,listOf(Item(0,"",0.0))))
    }*/
    fun insert(formula: Formula?) {
        if (formula != null) {
            repository.insert(formula)
        }
    }
    fun findFormula(id:Int):Formula?{
        return repository.findFormula(id)
    }
    fun update(formula: Formula) = repository.update(formula)

}

