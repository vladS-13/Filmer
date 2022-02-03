package com.example.filmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

var mapGenres: MutableMap<Int, String>? = null
var mapLanguages: MutableMap<String, String>? = null

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton: Button = findViewById(R.id.search)
        val myListButton: Button = findViewById(R.id.my_list_button)
        val quickSearch: Button = findViewById(R.id.quick_search)
        val aboutButton: Button = findViewById(R.id.info)
        searchButton.setOnClickListener {
            intent = Intent(this, SearchScreen::class.java)
            startActivity(intent)
        }
        myListButton.setOnClickListener {
            intent = Intent(this, MyListScreen::class.java)
            startActivity(intent)
        }
        aboutButton.setOnClickListener {
            intent = Intent(this, InfoScreen::class.java)
            startActivity(intent)
        }
        quickSearch.setOnClickListener {
            val urlResult = "https://api.themoviedb.org/3/discover/movie?" +
                    "api_key=${BuildConfig.API_KEY}&" +
                    "language=en-US&" +
                    "vote_count.gte=200&" +
                    "include_adult=false&" +
                    "include_video=false&" +
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
                    }else{
                        Toast.makeText(it, "We didn't find any movie", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        val urlGenres: String = "https://api.themoviedb.org/3/genre/movie/list?api_key=${BuildConfig.API_KEY}&language=en-US"
        val urlLanguage: String = "https://api.themoviedb.org/3/configuration/languages?api_key=${BuildConfig.API_KEY}"

        doAsync {
            var apiResponse = URL(urlGenres).readText()
            val allGenres = JSONObject(apiResponse).getJSONArray("genres")
            mapGenres = mutableMapOf()
            for (n in 0 until allGenres.length()) {
                val idGenres: Int = allGenres.getJSONObject(n).getInt("id")
                val nameGenres: String = allGenres.getJSONObject(n).getString("name")
                mapGenres!![idGenres] = nameGenres
            }

            apiResponse = URL(urlLanguage).readText()
            val allLanguages = JSONArray(apiResponse)
            mapLanguages = mutableMapOf()
            for (n in 0 until allLanguages.length()) {
                val isoLang: String = allLanguages.getJSONObject(n).getString("iso_639_1")
                val nameLanguage: String = allLanguages.getJSONObject(n).getString("english_name")
                mapLanguages!![isoLang] = nameLanguage
            }
        }
    }

    override fun onBackPressed() {
        // don't back to splash screen from main by press back button
    }
}