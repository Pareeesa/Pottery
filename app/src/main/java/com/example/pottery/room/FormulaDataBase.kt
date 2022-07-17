package com.example.pottery.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Formula::class,Item::class], version = 2)

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
                            .addMigrations(MIGRATION_1_2())
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
   class MIGRATION_1_2 : Migration(1,2){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Formula ADD COLUMN imagePath TEXT NOT NULL DEFAULT 0")
        }
    }
}
