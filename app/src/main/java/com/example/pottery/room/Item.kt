package com.example.pottery.room


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var code :String,
    var formulaName:String,
    var material:String,
    var amount : Int
    ) : Parcelable