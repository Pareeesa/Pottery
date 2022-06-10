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
    val itemListLiveData = MutableLiveData<MutableList<NewItem>>()
    val searchQuery = MutableLiveData("")
    private val formulaMap =Transformations.switchMap(searchQuery) {
        repository.searchDb(it)
    }
    val formulaList: LiveData<List<Formula>?> = formulaMap

    fun insertFormula(formula: Formula) {
        repository.insert(formula)
    }
    fun findFormulaByName(name:String): LiveData<FormulaWithItems>? {
        return repository.findFormulaByName(name)
    }

    fun isNewItemRepeated(item: NewItem): Boolean {
        if (itemListLiveData.value?.size == null || itemListLiveData.value?.size==0)
            return false
        else if (itemListLiveData.value?.contains(item)!!)
            return true
        return false
    }
    fun itemIsRepeated(itemCode: String,formulaName:String):Boolean{
        return repository.itemIsRepeated(itemCode,formulaName)
    }
    fun deleteFormula(formula:Formula) {
        repository.deleteFormula(formula)
        repository.deleteItems(formula.formulaName)
    }
    fun isFormulaNew(name: String):Boolean{
        return repository.isFormulaNew(name)
    }

    fun insertItems(formulaName:String) {
        if (itemListLiveData.value != null){
            for (i in itemListLiveData.value!!){
                repository.addItem(Item(0,i.code,formulaName,i.material,i.amount))
            }
        }
    }
    fun addItem(item: Item) = repository.addItem(item)

    fun addItemToList(item: NewItem) {
        if (itemListLiveData.value == null){
            itemListLiveData.value = mutableListOf(item)
        }else{
            val new = itemListLiveData.value
            if (new != null) {
                new.add(item)
                itemListLiveData.value = new!!
            }

            //itemListLiveData.value = itemListLiveData.value?.plus(item)
        }
    }
    fun updateItem(item: Item) = repository.updateItem(item)

}
data class NewItem(var code :String,var material:String, var amount : Double)

