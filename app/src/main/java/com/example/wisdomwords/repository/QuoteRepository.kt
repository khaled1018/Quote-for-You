package com.example.wisdomwords.repository

import androidx.lifecycle.LiveData
import com.example.wisdomwords.data.QuoteDao
import com.example.wisdomwords.model.QuoteData

class QuoteRepository(private val quoteDao: QuoteDao) {

    val readAllData: LiveData<MutableList<QuoteData>> = quoteDao.readAllQuotes()

    suspend fun addQuote(quote: QuoteData){
        quoteDao.insertQuote(quote)
    }

    suspend fun deleteQuote(quoteData: QuoteData){
        quoteDao.deleteQuote(quoteData)
    }

//    suspend fun deleteQuoteById(quoteId: Long){
//        quoteDao.deleteQuoteById(quoteId)
//    }
//
//    fun getQuoteByText(quote: String, author: String): LiveData<QuoteData?> {
//        return quoteDao.getQuoteByText(quote, author)
//    }

}