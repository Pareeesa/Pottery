package com.example.pottery.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.pottery.room.Formula
import com.example.pottery.room.FormulaRepository
import com.example.pottery.room.FormulaWithItems
import com.example.pottery.room.Item

class FormulaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FormulaRepository = FormulaRepository(application)
    val itemListLiveData = MutableLiveData<List<Item>>()
    val searchQuery = MutableLiveData("")
    private val formulaMap =Transformations.switchMap(searchQuery) {
        repository.searchDb(it)
    }
    val formulaList: LiveData<List<Formula>?> = formulaMap

    fun addItem(item: Item) {
        repository.addItem(item)
        if (itemListLiveData.value == null){
            itemListLiveData.value = listOf(item)
        }else{
        itemListLiveData.value = itemListLiveData.value?.plus(item)
        }
    }

    fun insertFormula(formula: Formula) {
        repository.insert(formula)
    }

    fun update(formula: Formula) {
        repository.update(formula)
    }

    fun findFormulaByName(name:String): LiveData<FormulaWithItems>? {
        return repository.findFormulaByName(name)
    }

    fun isItemRepeated(item: Item): Boolean {
        return repository.isItemRepeated(item)
    }

    fun deleteFormula(formula:Formula) {
        repository.deleteFormula(formula)
    }
    fun deleteItem(item: Item){
        repository.deleteItem(item)
    }
    fun isFormulaNew(name: String):Boolean{
        return repository.isFormulaNew(name)
    }
}

