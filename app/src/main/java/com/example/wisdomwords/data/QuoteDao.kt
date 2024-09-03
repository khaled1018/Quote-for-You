
package com.example.wisdomwords.data
import com.example.wisdomwords.model.QuoteData
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query



@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuote(quoteData: QuoteData)

    @Query("SELECT * FROM quotes ORDER BY id DESC")
    fun readAllQuotes(): LiveData<MutableList<QuoteData>>

    @Delete
    suspend fun deleteQuote(quoteData: QuoteData)

    // Query to delete last quote added to favourites
//    @Query("DELETE FROM quotes WHERE id = :quoteId")
//    suspend fun deleteQuoteById(quoteId: Long?)
}