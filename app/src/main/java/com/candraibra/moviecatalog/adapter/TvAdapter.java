package com.candraibra.moviecatalog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.model.Tv;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.MyViewHolder2> {

    private Context mContext;
    private ArrayList<Tv> tvList;

    public TvAdapter(Context mContext) {
        this.mContext = mContext;
        this.tvList = new ArrayList<>();
    }

    public void setTvList(ArrayList<Tv> tvList) {
        this.tvList = tvList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvAdapter.MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_popular_tv, viewGroup, false);

        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvAdapter.MyViewHolder2 holder, int i) {
        holder.tvTitle2.setText(tvList.get(i).getName());
        String poster = tvList.get(i).getPosterPath();
        Picasso.get().load(poster).placeholder(R.drawable.progress_animation).error(R.drawable.error).into(holder.imgPhoto2);

    }

    @Override
    public int getItemCount() {
        return tvList.size();
    }

    class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView tvTitle2;
        ImageView imgPhoto2;

        MyViewHolder2(View view) {
            super(view);

            tvTitle2 = itemView.findViewById(R.id.tv_title2);
            imgPhoto2 = itemView.findViewById(R.id.img_poster2);

        }
    }
}
