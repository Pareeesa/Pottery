package com.example.pottery.room

import android.app.Application
import androidx.lifecycle.LiveData

class FormulaRepository(application: Application?) {

    private var formulaDao: FormulaDao? = null
    private var formulaList: LiveData<List<Formula>?>? = null

    init {
        formulaRepository(application)
    }

    private fun formulaRepository(application: Application?) {
        val db = FormulaDataBase.getDatabase(application!!)
        formulaDao = db!!.formulaDao()
        formulaList = formulaDao!!.getAll()
    }

    fun getAllFormula(): LiveData<List<Formula>?>? {
        return formulaList
    }

    fun insert(formula: Formula) = formulaDao?.addFormula(formula)
    fun update(formula: Formula) = formulaDao?.update(formula)

    fun findFormula(id:Int):Formula?{
        return formulaDao?.findFormulaById(id)
    }
    fun findFormulaByName(name:String):Formula?{
        return formulaDao?.findFormulaByName(name)
    }
}