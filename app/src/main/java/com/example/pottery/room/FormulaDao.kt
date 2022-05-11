package com.example.pottery.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FormulaDao {

    @Insert
    fun addFormula(formula: Formula)

    @Insert
    fun addItem(vararg item: Item)

    @Query("SELECT Count(formulaName) FROM Formula WHERE formulaName = :name")
    fun isFormulaNew(name:String):Int

    @Query("SELECT * FROM Formula")
    fun getAll():LiveData<List<Formula>?>?

    @Query("SELECT * FROM Formula WHERE formulaName = :formulaName")
    fun getFormulaWithItems(formulaName : String ):LiveData<List<FormulaWithItems>?>?

    @Query("SELECT * FROM Formula WHERE formulaName LIKE '%' || :searchedQuery || '%' ORDER BY formulaName DESC")
    fun search(searchedQuery:String):LiveData<List<Formula>?>?

    @Update
    fun update(formula: Formula)

    @Delete
    fun delete(formula: Formula)

    @Query("DELETE FROM Formula")
    fun deleteAll()
}