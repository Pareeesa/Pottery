package com.example.pottery.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Formula(
    @PrimaryKey(autoGenerate = true) var id:Int,
    var formulaName : String,
    var imagePath:String)