package com.example.pottery.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ItemDao {

    @Query("SELECT * FROM Item WHERE formulaName = :formulaName AND code = :itemCode")
    fun itemIsRepeated(itemCode: String,formulaName:String):Int
}