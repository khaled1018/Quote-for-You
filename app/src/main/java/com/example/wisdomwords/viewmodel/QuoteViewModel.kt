package com.example.wisdomwords.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.wisdomwords.data.QuoteDatabase
import com.example.wisdomwords.model.QuoteData
import com.example.wisdomwords.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<MutableList<QuoteData>>
    private val repository: QuoteRepository

    init {
        val quoteDao = QuoteDatabase.getDatabase(application).quoteDao()
        repository = QuoteRepository(quoteDao)
        readAllData = repository.readAllData
    }

    fun addQuote(quoteData: QuoteData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addQuote(quoteData)
        }
    }

    fun deleteQuote(quoteData: QuoteData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQuote(quoteData)
        }
    }

//    fun deleteQuoteById(quoteId: Long){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteQuoteById(quoteId)
//        }
//    }
//
//    fun getQuoteByText(quote: String, author: String): LiveData<QuoteData?>{
//        return repository.getQuoteByText(quote, author)
//    }

}