package com.example.pottery.room

import androidx.room.Embedded
import androidx.room.Relation

data class FormulaWithItems(
    @Embedded
    var formula: Formula,
    @Relation(
        parentColumn = "formulaName",
        entityColumn = "formulaName",
    )
    val items :List<Item>
)