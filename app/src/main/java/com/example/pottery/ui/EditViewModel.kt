package com.example.pottery.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.pottery.room.Formula
import com.example.pottery.room.FormulaRepository
import com.example.pottery.room.FormulaWithItems
import com.example.pottery.room.Item

class EditViewModel(app:Application):AndroidViewModel(app) {

    private val repository: FormulaRepository = FormulaRepository(app)

    fun update(formula: Formula) {
        repository.update(formula)
    }

    fun findFormulaByName(name:String): LiveData<FormulaWithItems>? {
         return repository.findFormulaByName(name)
    }

    fun deleteItem(item: Item){
        repository.deleteItem(item)
    }
    fun updateItems(list: List<Item>,formulaName:String){
        for(i in list){
            updateItem(Item(i.id,i.code,formulaName,i.material,i.amount))
        }
    }
    fun updateItem(item: Item) = repository.updateItem(item)
}