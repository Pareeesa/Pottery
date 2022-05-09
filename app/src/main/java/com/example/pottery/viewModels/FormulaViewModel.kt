package com.example.pottery.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pottery.room.Formula
import com.example.pottery.room.FormulaRepository
import com.example.pottery.room.Item

class FormulaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FormulaRepository = FormulaRepository(application)
    val formulaList: LiveData<List<Formula>?>? = repository.getAllFormula()
    val itemListLiveData = MutableLiveData<List<Item>>()

    fun addItem(item: Item,formulaName:String) {
        if (itemListLiveData.value != null)
            itemListLiveData.value = itemListLiveData.value?.plus(item)
        else
            itemListLiveData.value = listOf(item)
        val formula = repository.findFormulaByName(formulaName)
        if (formula != null)
            repository.update(Formula(formula.id,formulaName, itemListLiveData.value!!))
    }

    fun insert(formula: Formula?) {
        if (formula != null) {
            repository.insert(formula)
        }
    }
    fun findFormula(id:Int):Formula?{
        return repository.findFormula(id)
    }
    fun update(formula: Formula) = repository.update(formula)

    fun findFormulaByName(name:String):Formula?{
        return repository.findFormulaByName(name)
    }

    fun itemIsNew(item: Item,name: String): Boolean {
        val formula = findFormulaByName(name)
        if (formula != null)
            return !(formula.itemList.contains(item))

        return true
    }

    fun deleteFormula(formula:Formula) {
        repository.deleteFormula(formula)
    }

}

