package com.candraibra.moviecatalog.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.FavViewHolder> {
    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    private final ArrayList<Movie> movieList = new ArrayList<>();
    private final Activity activity;

    public FavMovieAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setMovieList(ArrayList<Movie> movieList) {

        if (movieList.size() > 0) {
            this.movieList.clear();
        }
        this.movieList.addAll(movieList);

        notifyDataSetChanged();
    }

    public void addItem(Movie movie) {
        this.movieList.add(movie);
        notifyItemInserted(movieList.size() - 1);
    }

    public void removeItem(int position) {
        this.movieList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, movieList.size());
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_movie, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        holder.tvTitle.setText(movieList.get(position).getTitle());
        String poster = movieList.get(position).getPosterPathFav();
        Picasso.get().load(poster).placeholder(R.drawable.load).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class FavViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgPhoto;
        FavViewHolder(@NonNull View itemView) {
            super(itemView);
           tvTitle = itemView.findViewById(R.id.tv_title);
            imgPhoto = itemView.findViewById(R.id.img_poster);
        }
    }
}
