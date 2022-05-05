package com.example.pottery.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Formula::class], version = 1)
@TypeConverters(Converters::class)
abstract class FormulaDataBase : RoomDatabase() {

    abstract fun formulaDao(): FormulaDao?

    companion object {
        private var INSTANCE: FormulaDataBase? = null
        fun getDatabase(context: Context): FormulaDataBase? {
            if (INSTANCE == null) {
                synchronized(FormulaDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            FormulaDataBase::class.java, "formula_database"
                        ).allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
