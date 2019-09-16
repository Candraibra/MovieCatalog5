package com.candraibra.moviecatalog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviePageAdapter extends RecyclerView.Adapter<MoviePageAdapter.MyViewHolder> {

    private ArrayList<Movie> movieList;

    public MoviePageAdapter(Context mContext) {
        this.movieList = new ArrayList<>();
    }


    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;

    }

    public void appendMovies(ArrayList<Movie> moviesToAppend) {
        movieList.addAll(moviesToAppend);

    }

    @NonNull
    @Override
    public MoviePageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_discover_movie, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePageAdapter.MyViewHolder holder, int i) {
        String poster = movieList.get(i).getPosterPathMini();
        Picasso.get().load(poster).placeholder(R.drawable.progress_animation).error(R.drawable.error).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;

        MyViewHolder(View view) {
            super(view);

            imgPhoto = itemView.findViewById(R.id.img_poster);

        }
    }
}
