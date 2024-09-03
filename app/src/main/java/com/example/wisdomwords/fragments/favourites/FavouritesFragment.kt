package com.example.wisdomwords.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wisdomwords.R
import com.example.wisdomwords.viewmodel.QuoteViewModel


class FavouritesFragment : Fragment() {

    private lateinit var viewModel: QuoteViewModel
    private lateinit var adapter: QuoteAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        requireActivity().title = "Favourites"

        // QuoteViewModel
        viewModel = ViewModelProvider(this)[QuoteViewModel::class.java]

        // RecyclerView
        adapter = QuoteAdapter(viewModel)
        recyclerView = view.findViewById(R.id.recyclerView_favouriteQuotes)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)



        viewModel.readAllData.observe(viewLifecycleOwner) { quotes ->
            quotes?.let {
                adapter.setData(it)
            }
        }
        return view
    }
}