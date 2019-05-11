package com.example.week5

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment.*
import java.io.IOException
import okhttp3.*
import com.google.gson.GsonBuilder
import android.os.AsyncTask.execute
import android.os.Handler
import android.support.v4.app.FragmentActivity
import com.google.gson.Gson
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.Log
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.util.*


class nowPlayingFragment : Fragment() {
//    interface Listener {
//        fun putDataNow()
//        fun openProfileFilm()
//    }

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable

    var movies: ArrayList<MovieModel.Results> = ArrayList()
    lateinit var movieAdapter: MovieAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the handler instance
        mHandler = Handler()

        swipeContainer.setOnRefreshListener {

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/now_playing?api_key=7519cb3f829ecd53bd9b7007076dbe23")
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
                        Log.i("JSON", json.toString())
                        val jsObect = JSONObject(json)
                        val result = jsObect.getJSONArray("results").toString()
                        val collectionType = object : TypeToken<Collection<MovieModel.Results>>() {}.type
                        movies = Gson().fromJson(result, collectionType)
                        Log.i("PARCEL: ", movies.toString())
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

            mRunnable = Runnable {
                // Hide swipe to refresh icon animation
                swipeContainer.isRefreshing = false
            }

            // Execute the task after specified time
            mHandler.postDelayed(
                mRunnable,
                (100).toLong() // Delay 1 seconds
            )
        }
    }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/now_playing?api_key=7519cb3f829ecd53bd9b7007076dbe23")
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
                    Log.i("JSON", json.toString())
                    val jsObect = JSONObject(json)
                    val result = jsObect.getJSONArray("results").toString()
                    val collectionType = object : TypeToken<Collection<MovieModel.Results>>() {}.type
                    movies = Gson().fromJson(result, collectionType)
                    Log.i("PARCEL: ", movies.toString())
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
    private val MovieItemClickListener = object : MovieItemClickListener {
        override fun onItemCLicked(position: Int) {
            Log.i("Now Playing: ", "Hello")
            val intent : Intent = Intent(activity, ProfileFilm::class.java)
            intent.putExtra("FILM_KEY", movies.get(position))
            startActivity(intent)

        }

    }

}



