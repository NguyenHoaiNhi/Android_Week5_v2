package com.example.week5

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*
@SuppressLint("SetTextI18n")

class MovieAdapter (var items: ArrayList<MovieModel.Results>, val context: Context) : RecyclerView.Adapter<MovieViewHolder>() {
    lateinit var mListener: MovieItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(movieViewHolder: MovieViewHolder, position: Int) {
        //movieViewHolder.ivAvatar = "#$position ${items[position].}"
        movieViewHolder.tvName.text = items.get(position).title
        movieViewHolder.tvReleaseDate.text = "Release date: "+items.get(position).release_date
        movieViewHolder.tvLanguage.text = "Original language: "+items.get(position).original_language
        movieViewHolder.tvPopularity.text = "Popularity: "+items.get(position).popularity
        movieViewHolder.tvVote.text = "Vote count: "+items.get(position).vote_count
        var url : String? = "https://image.tmdb.org/t/p/w500/"+items[position].poster_path
        Glide.with(context)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.student_place_holder)
            .into(movieViewHolder.tvAvatar)


        movieViewHolder.itemView.setOnClickListener {
            mListener.onItemCLicked(position)
        }


        // movieViewHolder.itemView.info.setOnClickListener(toDetail)

    }
    fun setListener(listener: MovieItemClickListener) {
        this.mListener = listener
    }

//    fun setData(items: ArrayList<Student>){
//        this.items = items
//        notifyDataSetChanged()
//    }
}


class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvAvatar = view.ivAvatar
    var tvName = view.NameFilm
    var tvPlay = view.play
    var tvReleaseDate = view.Release
    var tvLanguage = view.language
    var tvPopularity = view.popularity
    var tvVote = view.vote_count
}