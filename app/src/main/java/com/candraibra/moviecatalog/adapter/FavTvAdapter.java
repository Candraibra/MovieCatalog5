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
import com.candraibra.moviecatalog.model.Tv;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavTvAdapter extends RecyclerView.Adapter<FavTvAdapter.FavViewHolder> {
    public ArrayList<Tv> getTvList() {
        return tvList;
    }

    private final ArrayList<Tv> tvList = new ArrayList<>();
    private final Activity activity;

    public FavTvAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setTvList(ArrayList<Tv> tvList) {

        if (tvList.size() > 0) {
            this.tvList.clear();
        }
        this.tvList.addAll(tvList);

        notifyDataSetChanged();
    }

    public void addItem(Tv tv) {
        this.tvList.add(tv);
        notifyItemInserted(tvList.size() - 1);
    }

    public void removeItem(int position) {
        this.tvList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tvList.size());
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_movie, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        holder.tvTitle.setText(tvList.get(position).getName());
        String poster = tvList.get(position).getPosterPathFav();
        Picasso.get().load(poster).placeholder(R.drawable.loading).error(R.drawable.error).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return tvList.size();
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
