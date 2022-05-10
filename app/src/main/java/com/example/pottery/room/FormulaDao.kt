package com.example.pottery.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FormulaDao {

    @Insert
    fun addFormula(formula: Formula)

    @Query("SELECT * FROM Formula")
    fun getAll():LiveData<List<Formula>?>?

    @Query("SELECT * FROM Formula WHERE id = (:id)")
    fun findFormulaById(id:Int):Formula?

    @Query("SELECT * FROM Formula WHERE name = (:name)")
    fun findFormulaByName(name:String):Formula?

    @Query("SELECT * FROM Formula WHERE name LIKE '%' || :searchedQuery || '%' ORDER BY name DESC")
    fun search(searchedQuery:String):LiveData<List<Formula>?>?

    @Update
    fun update(formula: Formula)

    @Delete
    fun delete(formula: Formula)

    @Query("DELETE FROM Formula")
    fun deleteAll()
}