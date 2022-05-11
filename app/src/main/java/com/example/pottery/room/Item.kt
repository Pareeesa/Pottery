package com.example.pottery.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var code :String,
    var formulaName:String,
    var material:String,
    var amount : Int
    )