package com.example.pottery.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FormulaDao {

    @Insert
    fun addFormula(formula: Formula)

    @Query("SELECT * FROM Formula")
    fun getAll():LiveData<List<Formula>>
}