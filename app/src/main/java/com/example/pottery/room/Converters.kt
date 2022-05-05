package com.example.pottery.room

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun stringFromItemList(itemList: List<Item>): String {
        var string = ""
        for (item in itemList)
            string += item.id.toString() + "," + item.material + "," + item.amount.toString() + "-"
        return string
    }

    @TypeConverter
    fun stringToItem(string: String): List<Item> {

        val itemList = arrayListOf<Item>()
        val items = string.split('-')
        for (str in items){
            if (str.isBlank())
                break
            val item = str.split(',')
            itemList.add(Item(item[0].toInt(),item[1],item[2].toDouble()))
        }
        return itemList
    }
}