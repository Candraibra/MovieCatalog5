package com.candraibra.moviecatalog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.model.Tv;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TvPageAdapter extends RecyclerView.Adapter<TvPageAdapter.MyViewHolder> {

    private ArrayList<Tv> tvList;

    public TvPageAdapter(Context mContext) {
        this.tvList = new ArrayList<>();
    }

    public void setTvList(ArrayList<Tv> tvList) {
        this.tvList = tvList;

    }

    public void appendTv(ArrayList<Tv> tvToAppend) {
        tvList.addAll(tvToAppend);

    }

    @NonNull
    @Override
    public TvPageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_discover_movie, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvPageAdapter.MyViewHolder holder, int i) {
        String poster = tvList.get(i).getPosterPathMini();
        Picasso.get().load(poster).placeholder(R.drawable.loading).error(R.drawable.error).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return tvList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;

        MyViewHolder(View view) {
            super(view);
            imgPhoto = itemView.findViewById(R.id.img_poster);

        }
    }
}
