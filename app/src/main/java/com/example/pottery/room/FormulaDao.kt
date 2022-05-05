package com.example.pottery.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FormulaDao {

    @Insert
    fun addFormula(formula: Formula)

    @Query("SELECT * FROM Formula")
    fun getAll():LiveData<List<Formula>?>?

    @Query("SELECT * FROM Formula WHERE id = (:id)")
    fun findFormulaById(id:Int):Formula?

    @Update
    fun update(formula: Formula)
}