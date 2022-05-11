package com.example.pottery.room

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class FormulaRepository(application: Application?) {

    private var formulaDao: FormulaDao? = null
    private var itemDao: ItemDao? = null
    private var formulaList: LiveData<List<Formula>?>? = null

    init {
        formulaRepository(application)
    }

    private fun formulaRepository(application: Application?) {
        val db = FormulaDataBase.getDatabase(application!!)
        formulaDao = db!!.formulaDao()
        formulaList = formulaDao!!.getAll()
    }

    fun insert(formula: Formula) = formulaDao?.addFormula(formula)
    fun update(formula: Formula) = formulaDao?.update(formula)

    fun findFormulaByName(name:String):LiveData<List<FormulaWithItems>?>?{
        return formulaDao?.getFormulaWithItems(name)
    }
    fun deleteFormula(formula:Formula) {
        formulaDao?.delete(formula)
    }
    fun searchDb(searchQuery :String): LiveData<List<Formula>?>{
        return formulaDao?.search(searchQuery)!!
    }
    fun addItem(vararg item: Item) = formulaDao?.addItem(*item)

    fun isItemRepeated(item: Item):Boolean{
        val result = itemDao?.itemIsRepeated(item.code,item.material)
        return result == 1
    }
    fun isFormulaNew(name: String):Boolean{
       return (formulaDao?.isFormulaNew(name) == 0)
    }
}