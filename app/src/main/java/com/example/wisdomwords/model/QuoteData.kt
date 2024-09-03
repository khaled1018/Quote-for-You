package com.example.wisdomwords.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "quotes"
)
data class QuoteData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quote: String,
    val author: String
)
