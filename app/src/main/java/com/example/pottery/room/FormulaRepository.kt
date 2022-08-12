package com.example.pottery.room

import android.app.Application
import androidx.lifecycle.LiveData

class FormulaRepository(application: Application?) {

    private var formulaDao: FormulaDao? = null
    private var formulaList: LiveData<List<Formula>?>? = null

    init {
        formulaRepository(application)
    }

    fun getAllFormulas() = formulaDao?.getAllFormulas()

    private fun formulaRepository(application: Application?) {
        val db = FormulaDataBase.getDatabase(application!!)
        formulaDao = db!!.formulaDao()
        formulaList = formulaDao!!.getAll()
    }

    fun insert(formula: Formula) = formulaDao?.addFormula(formula)
    fun update(formula: Formula) = formulaDao?.update(formula)
    fun updateItem(item: Item) = formulaDao?.updateItem(item)

    fun findFormulaByName(name:String): LiveData<FormulaWithItems>? {
        return formulaDao?.getFormulaWithItems(name)
    }
    fun deleteFormula(formula:Formula) {
        formulaDao?.delete(formula)
    }
    fun searchDb(searchQuery :String): LiveData<List<Formula>?>{
        return formulaDao?.search(searchQuery)!!
    }
    fun addItem(vararg item: Item) = formulaDao?.addItem(*item)

    fun isFormulaNew(name: String):Boolean{
       return (formulaDao?.isFormulaNew(name) == 0)
    }
    fun deleteItem(item: Item) {
        formulaDao?.deleteItem(item)
    }
    fun deleteItems(formulaName :String) = formulaDao?.deleteItems(formulaName)

    fun itemIsRepeated(itemCode: String,formulaName:String):Boolean{
        return formulaDao?.itemIsRepeated(itemCode,formulaName) == 1
    }


}