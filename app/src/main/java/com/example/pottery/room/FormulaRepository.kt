package com.example.pottery.room

import android.content.Context

class FormulaRepository {

    var formulaDatabase : FormulaDataBase?=null
    var formulaDao : FormulaDao?=null

    fun initDB(context: Context){
        formulaDatabase= FormulaDataBase.getDataBase(context)
        formulaDao=formulaDatabase?.formulaDao()
    }

    fun addFormula(formula: Formula) = formulaDao?.addFormula(formula)
}