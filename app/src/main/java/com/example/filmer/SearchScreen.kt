package com.example.filmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.slider.RangeSlider
import org.jetbrains.anko.*
import org.json.JSONObject
import java.net.URL

class SearchScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_screen)
        val genresSpinner: Spinner = findViewById(R.id.genresSpinner)
        val languageTextView: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val startButton: Button = findViewById(R.id.start)
        val adultCheckBox: CheckBox = findViewById(R.id.adultCheckBox)
        val rangeSlider: RangeSlider = findViewById(R.id.rate)
        val yearFrom: TextView = findViewById(R.id.fromYear)
        val yearTo: TextView = findViewById(R.id.toYear)
        genresSpinner.adapter = ArrayAdapter(
                        genresSpinner.context,
                        android.R.layout.simple_spinner_item,
                        ArrayList(mapGenres!!.values))
        languageTextView.setAdapter(ArrayAdapter(
                        languageTextView.context,
                        android.R.layout.simple_dropdown_item_1line,
                        ArrayList(mapLanguages!!.values)))


        startButton.setOnClickListener {
            val originalLanguage = if(languageTextView.text.toString() != "" && mapLanguages?.values?.contains(languageTextView.text.toString()) == true){
                mapLanguages?.filterValues { it == languageTextView.text.toString() }?.keys?.elementAt(0)
            } else {
                ""
            }

            val withGenre: Int? = mapGenres?.filterValues { it == genresSpinner.selectedItem.toString()}?.keys?.elementAt(0)
            val urlResult = "https://api.themoviedb.org/3/discover/movie?" +
                    "api_key=${BuildConfig.API_KEY}&" +
                    "language=en-US&" +
                    "vote_count.gte=200&" +
                    "include_adult=${adultCheckBox.isChecked}&" +
                    "include_video=false&" +
                    getYear(yearFrom, yearTo) +
                    "vote_average.gte=${rangeSlider.values[0]}&" +
                    "vote_average.lte=${rangeSlider.values[1]}&" +
                    "with_genres=$withGenre&" +
                    "with_original_language=$originalLanguage&" +
                    "with_watch_monetization_types=flatrate&" +
                    "page=1"
            doAsync {
                val apiResponse = URL(urlResult).readText()
                val totalResults = JSONObject(apiResponse).getInt("total_results")
                val totalPages = JSONObject(apiResponse).getInt("total_pages")
                uiThread {
                    if(totalResults > 0){
                        intent = Intent(it, FilmScreen::class.java)
                        intent.putExtra("discoverUrl", urlResult)
                        intent.putExtra("totalResults", totalResults)
                        intent.putExtra("totalPages", totalPages)
                        startActivity(intent)
                    } else{
                        Toast.makeText(it, "We didn't find any movie", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getYear(fromY: TextView, toY: TextView) : String {
        val fromStr = fromY.text.toString()
        val toStr = toY.text.toString()
        return if(fromStr == "" && toStr == ""){
            ""
        } else if (fromStr != "" && toStr == ""){
            "primary_release_date.gte=$fromStr-01-01&"
        } else if (fromStr == "" && toStr != ""){
            "primary_release_date.lte=$toStr-12-31&"
        } else if (fromStr.toInt() > toStr.toInt()){
            Toast.makeText(this,"Years are ignored", Toast.LENGTH_SHORT).show()
            ""
        } else {
            "primary_release_date.gte=$fromStr-01-01&primary_release_date.lte=$toStr-12-31&"
        }
    }
}