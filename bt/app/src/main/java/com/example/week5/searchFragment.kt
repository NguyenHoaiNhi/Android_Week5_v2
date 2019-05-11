package com.example.week5

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment.*
import okhttp3.*
import java.io.IOException
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.search_movie.*
import org.json.JSONObject

class searchFragment :Fragment() {
    var movies: ArrayList<MovieModel.Results> = ArrayList()
    lateinit var movieAdapter: MovieAdapter

    val textString: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.fragment_search, container, false)

        val view: View = inflater!!.inflate(R.layout.fragment_search, container, false)

        val btn: Button = view.findViewById(R.id.btnGetData)

        btn.setOnClickListener{ view ->
            Log.d("btnSetup", "Selected")
            createFragment()
        }
        val text = view.findViewById<EditText>(R.id.edtUserID)


        return view
    }

    private fun createFragment()
    {
        val query = edtUserID.text.toString()
        val client = OkHttpClient()
        Log.d("Query", query)
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/search/movie?api_key=7519cb3f829ecd53bd9b7007076dbe23&query=" + query)
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    getActivity()?.runOnUiThread(Runnable {
                        print("nothing")
                    })

                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()!!.string()
                    val jsObect = JSONObject(json)
                    val result = jsObect.getJSONArray("results").toString()
                    val collectionType = object : TypeToken<Collection<MovieModel.Results>>() {}.type
                    movies = Gson().fromJson(result, collectionType)

                    getActivity()?.runOnUiThread(Runnable {
                        rvMovies.apply {
                            layoutManager = LinearLayoutManager(context)
                            movieAdapter = MovieAdapter(movies, context)
                            adapter = movieAdapter
                            movieAdapter.setListener(MovieItemClickListener)
                        }
                    })

                }

            })
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://api.themoviedb.org/3/search/movie?api_key=7519cb3f829ecd53bd9b7007076dbe23&query=avenger")
//            .build()
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    getActivity()?.runOnUiThread(Runnable {
//                        print("nothing")
//                    })
//
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    val json = response.body()!!.string()
//                    val jsObect = JSONObject(json)
//                    val result = jsObect.getJSONArray("results").toString()
//                    val collectionType = object : TypeToken<Collection<MovieModel.Results>>() {}.type
//                    movies = Gson().fromJson(result, collectionType)
//
//                    getActivity()?.runOnUiThread(Runnable {
//                        rvMovies.apply {
//                            layoutManager = LinearLayoutManager(context)
//                            movieAdapter = MovieAdapter(movies, context)
//                            adapter = movieAdapter
//                            movieAdapter.setListener(MovieItemClickListener)
//                        }
//                    })
//
//                }
//
//            })
//    }
    private val MovieItemClickListener = object : MovieItemClickListener {
        override fun onItemCLicked(position: Int) {
            Log.i("Search", "Hi")
//            val bundle = Bundle()
//            val fragmentGet = topRateFragment()
//            bundle.putParcelable("FILM_KEY", movies.get(position))
//            fragmentGet.arguments = bundle
            val intent : Intent = Intent(activity, ProfileFilm::class.java)
            intent.putExtra("FILM_KEY", movies.get(position))
            startActivity(intent)



        }

    }
}