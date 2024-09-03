package com.example.wisdomwords.fragments.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wisdomwords.R
import com.example.wisdomwords.model.QuoteData
import com.example.wisdomwords.viewmodel.QuoteViewModel

class QuoteAdapter(private val viewModel: QuoteViewModel) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    private var quoteList: MutableList<QuoteData> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuoteViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.one_line_quote, parent, false)
        return QuoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val currQuote = quoteList[position]
        holder.tvFavouriteQuote.text = currQuote.quote
        holder.tvAuthorFavouriteQuote.text = currQuote.author
        holder.btnRemoveQuote.setOnClickListener {
            viewModel.deleteQuote(currQuote)
            removeQuote(position)
        }
    }

    private fun removeQuote(position: Int){
        quoteList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, quoteList.size)
    }


    override fun getItemCount(): Int {
        return quoteList.size
    }

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFavouriteQuote: TextView = itemView.findViewById(R.id.tv_favouriteQuote)
        val tvAuthorFavouriteQuote: TextView = itemView.findViewById(R.id.tv_authorFavouriteQuote)
        val btnRemoveQuote: ImageButton = itemView.findViewById(R.id.btn_removeQuote)
    }

    fun setData(newQuotes: MutableList<QuoteData>) {
        val oldSize = quoteList.size
        quoteList = newQuotes

        if (oldSize < newQuotes.size) {
            notifyItemInserted(newQuotes.size)
        } else if (oldSize > newQuotes.size) {
            notifyItemRemoved(oldSize - 1)
        } else {
            notifyDataSetChanged()
        }

    }
}