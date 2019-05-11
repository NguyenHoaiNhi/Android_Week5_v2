package com.example.week5

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment.*
import okhttp3.*
import java.io.IOException
import android.util.Log
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class topRateFragment : Fragment() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable

    var movies: ArrayList<MovieModel.Results> = ArrayList()
    lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeContainer.setOnRefreshListener {

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/top_rated?api_key=7519cb3f829ecd53bd9b7007076dbe23")
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

            // Initialize the handler instance
            mHandler = Handler()

            mRunnable = Runnable {
                // Hide swipe to refresh icon animation
                swipeContainer.isRefreshing = false
            }

            // Execute the task after specified time
            mHandler.postDelayed(
                mRunnable, 100

            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/top_rated?api_key=7519cb3f829ecd53bd9b7007076dbe23")
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
    private val MovieItemClickListener = object : MovieItemClickListener {
        override fun onItemCLicked(position: Int) {
            Log.i("Top Rate", "Hi")
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