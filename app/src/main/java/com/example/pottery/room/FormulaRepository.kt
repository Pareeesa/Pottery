package com.example.pottery.room

import android.content.Context
import androidx.lifecycle.LiveData

class FormulaRepository {

    var formulaDatabase : FormulaDataBase?=null
    var formulaDao : FormulaDao?=null
    lateinit var formulaList : LiveData<List<Formula>>


    fun initDB(context: Context){
        formulaDatabase= FormulaDataBase.getDataBase(context)
        formulaDao=formulaDatabase?.formulaDao()
        testData()
        formulaList = formulaDao?.getAll()!!
    }

    fun addFormula(formula: Formula) = formulaDao?.addFormula(formula)
    fun testData() {
        formulaDao?.addFormula(Formula(1, listOf(Item(1,"sotb",200.0))))
        formulaDao?.addFormula(Formula(1, listOf(Item(1,"sotb",200.0))))
        formulaDao?.addFormula(Formula(1, listOf(Item(1,"sotb",200.0))))
        formulaDao?.addFormula(Formula(1, listOf(Item(1,"sotb",200.0))))
    }
}