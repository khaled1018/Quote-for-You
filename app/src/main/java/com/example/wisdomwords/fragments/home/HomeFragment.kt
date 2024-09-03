package com.example.wisdomwords.fragments.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.appgozar.fadeoutparticle.FadeOutParticleFrameLayout
import com.example.wisdomwords.ApiService.MySingleton
import com.example.wisdomwords.R
import com.example.wisdomwords.model.QuoteData
import com.example.wisdomwords.viewmodel.QuoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var viewModel: QuoteViewModel
    private var quoteData: QuoteData = QuoteData(0, "", "")
    private lateinit var tvQuote: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var btnNewQuote: Button
    private lateinit var btnFavouriteQuote: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var fadeOutParticleFrameLayout: FadeOutParticleFrameLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var isFavouriteQuote: Boolean = false
    private var favouriteQuoteId: Long = -1
    private var quoteString: String = ""
    private var authorString: String = ""

    private val url: String = "https://api.quotable.io/quotes/random"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        requireActivity().title = "Home"

        viewModel = ViewModelProvider(requireActivity())[QuoteViewModel::class.java]

        tvQuote = view.findViewById(R.id.tv_quote)
        tvAuthor = view.findViewById(R.id.tv_quoteAuthor)
        btnNewQuote = view.findViewById(R.id.btn_newQuote)
        btnFavouriteQuote = view.findViewById(R.id.btn_addToFavourites)
        fab = view.findViewById(R.id.fab)
        fadeOutParticleFrameLayout = view.findViewById(R.id.quoteLayout)

        fadeOutParticleFrameLayout.animationDuration = 5000L

        sharedPreferences = requireContext().getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

        checkFirstLaunch(isFirstLaunch)
        isFavouriteQuote = sharedPreferences.getBoolean("isFavourite", false)

        updateButtonImage()

        btnFavouriteQuote.setOnClickListener {

            isFavouriteQuote = !isFavouriteQuote
            updateButtonImage()
            saveButtonState(isFavouriteQuote)
            addToFavourites(isFavouriteQuote)
        }

        btnNewQuote.setOnClickListener {
            fadeOutParticleFrameLayout.startAnimation()

            getQuote(url) { quoteData ->
                quoteString = quoteData.quote
                authorString = quoteData.author

                // Update the quote and author text views after the animation completes
                fadeOutParticleFrameLayout.postDelayed({
                    tvQuote.text = quoteString
                    tvAuthor.text = authorString
                    fadeOutParticleFrameLayout.reset()
                    if (isFavouriteQuote) {
                        isFavouriteQuote = false
                        updateButtonImage()
                    }
                }, 2000)
            }
        }

        fab.setOnClickListener {
            val quoteText = tvQuote.text
            val authorText = tvAuthor.text

            if (quoteText.isNotEmpty() && authorText.isNotEmpty()) {
                shareContent(quoteText.toString(), authorText.toString())
            } else {
                Toast.makeText(requireContext(), "Text to share is empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view
    }

    private fun checkFirstLaunch(isFirstLaunch: Boolean) {
        if (isFirstLaunch){
            tvQuote.text = R.string.quote_text.toString()
            tvAuthor.text = R.string.author_text.toString()
            with(sharedPreferences.edit()){
                putBoolean("isFirstLaunch", false)
                apply()
            }
        }

    }

    private fun inputCheck(quote: String, author: String): Boolean {
        return !(TextUtils.isEmpty(quote) && TextUtils.isEmpty(author))
    }

    private fun addToFavourites(isFavouriteQuote: Boolean) {
        if (isFavouriteQuote) {
            val quote = tvQuote.text.toString()
            val author = tvAuthor.text.toString()
            if (inputCheck(quote, author)) {
                val newQuote = QuoteData(0, quote, author)
                viewModel.addQuote(newQuote)
                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveButtonState(isFavouriteQuote: Boolean) {
        sharedPreferences = requireContext().getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isFavourite", isFavouriteQuote)
        editor.apply()
    }

    private fun updateButtonImage() {
        if (isFavouriteQuote) {
            btnFavouriteQuote.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            btnFavouriteQuote.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun shareContent(quote: String, author: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$quote\nAuthor: $author")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun getQuote(url: String, callback: (QuoteData) -> Unit) {
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val jsonObject = response.getJSONObject(0)
                quoteData =
                    QuoteData(0, jsonObject.getString("content"), jsonObject.getString("author"))
                callback(quoteData)
            },
            { error ->
                Toast.makeText(requireContext(), "Something Wrong", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(requireContext()).addToRequestQueue(jsonArrayRequest)
    }

    override fun onResume() {
        super.onResume()
        val storedQuote = sharedPreferences.getString("quote", "")
        val storedAuthor = sharedPreferences.getString("author", "")
        val storedFavouriteQuoteId = sharedPreferences.getLong("quoteId", -1)

        tvQuote.text = storedQuote
        tvAuthor.text = storedAuthor
        favouriteQuoteId = storedFavouriteQuoteId
    }

    override fun onPause() {
        super.onPause()
        saveButtonState(isFavouriteQuote)
        editor = sharedPreferences.edit()
        editor.putString("quote", tvQuote.text.toString())
        editor.putString("author", tvAuthor.text.toString())
        editor.putLong("quoteId", favouriteQuoteId)
        editor.apply()
    }
}