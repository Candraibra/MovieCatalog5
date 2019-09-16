package com.candraibra.moviecatalog.adapter;

import android.content.Context;
import android.database.Cursor;
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

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.FavViewHolder> {
    private Cursor cursor;

    public FavMovieAdapter(Context context) {
        Context mContext = context;
    }

    public void setMovieList(Cursor movieList) {
        this.cursor = movieList;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_movie, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        final Movie result = getItem(position);
        holder.tvTitle.setText(result.getTitle());
        String poster = result.getPosterPathFav();
        Picasso.get().load(poster).placeholder(R.drawable.progress_animation).error(R.drawable.error).into(holder.imgPhoto);
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(cursor);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
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
