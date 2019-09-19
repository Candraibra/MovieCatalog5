package com.candraibra.moviecatalog.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.activity.DetailTvActivity;
import com.candraibra.moviecatalog.model.Tv;
import com.squareup.picasso.Picasso;

public class FavTvAdapter extends RecyclerView.Adapter<FavTvAdapter.FavViewHolder> {

    private Cursor tv_cursor;

    public FavTvAdapter(Context context) {
        Context mContext = context;
    }

    public void setTvList(Cursor tvList) {
        this.tv_cursor = tvList;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_movie, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        final Tv result = getItem(position);
        holder.tvTitle.setText(result.getName());
        String poster = result.getPosterPathFav();
        holder.imgPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailTvActivity.class);
            intent.putExtra(DetailTvActivity.EXTRA_TV, result);
            holder.itemView.getContext().startActivity(intent);
        });
        Picasso.get().load(poster).placeholder(R.drawable.load).into(holder.imgPhoto);
    }

    private Tv getItem(int position) {
        if (!tv_cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Tv(tv_cursor);
    }

    @Override
    public int getItemCount() {
        if (tv_cursor == null) return 0;
        return tv_cursor.getCount();
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
