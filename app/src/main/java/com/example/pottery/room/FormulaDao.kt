package com.example.pottery.room

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface FormulaDao {

    @Insert
    fun addFormula(formula: Formula)
}