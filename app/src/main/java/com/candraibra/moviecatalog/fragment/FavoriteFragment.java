package com.candraibra.moviecatalog.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.activity.DetailMovieActivity;
import com.candraibra.moviecatalog.activity.DetailTvActivity;
import com.candraibra.moviecatalog.adapter.FavMovieAdapter;
import com.candraibra.moviecatalog.adapter.FavTvAdapter;
import com.candraibra.moviecatalog.database.MovieHelper;
import com.candraibra.moviecatalog.database.TvHelper;
import com.candraibra.moviecatalog.utils.ItemClickSupport;

import java.util.Objects;

import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI;
import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI_TV;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    private final static String LIST_STATE_KEY = "STATE";
    private final static String LIST_STATE_KEY2 = "STATE2";
    private Cursor list_movie;
    private Cursor list_tv;
    private RecyclerView rvMovie, rvTv;
    private FavMovieAdapter favMovieAdapter;
    private FavTvAdapter favTvAdapter;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rv_liked_movie);
        rvTv = view.findViewById(R.id.rv_liked_tv);
        MovieHelper movieHelper = MovieHelper.getInstance(getActivity());
        movieHelper.open();
        TvHelper tvHelper = TvHelper.getInstance(getActivity());
        tvHelper.open();
        new loadMovie().execute();
        showRecyclerMovie();
        new loadTv().execute();
        showRecyclerTv();

    }

    private void showRecyclerMovie() {
        favMovieAdapter = new FavMovieAdapter(getActivity());
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvMovie.setAdapter(favMovieAdapter);
        rvMovie.setHasFixedSize(true);
        favMovieAdapter.setMovieList(list_movie);
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailTvActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, list_movie.getPosition());
            startActivity(intent);
        });
    }

    private void showRecyclerTv() {
        favTvAdapter = new FavTvAdapter(getActivity());
        rvTv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTv.setHasFixedSize(true);
        rvTv.setAdapter(favTvAdapter);
        favTvAdapter.setTvList(list_tv);
        ItemClickSupport.addTo(rvTv).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailTvActivity.class);
            intent.putExtra(DetailTvActivity.EXTRA_TV, list_tv.getPosition());
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * @Override public void onSaveInstanceState(@NonNull Bundle outState) {
     * super.onSaveInstanceState(outState);
     * outState.putParcelableArrayList(LIST_STATE_KEY, movieArrayList);
     * outState.putParcelableArrayList(LIST_STATE_KEY2, tvArrayList);
     * }
     */
    private class loadMovie extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getContext()).getContentResolver()
                    .query(
                            CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor_movie) {
            super.onPostExecute(cursor_movie);
            list_movie = cursor_movie;
            favMovieAdapter.setMovieList(list_movie);
            favMovieAdapter.notifyDataSetChanged();
        }
    }

    private class loadTv extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getContext()).getContentResolver()
                    .query(
                            CONTENT_URI_TV,
                            null,
                            null,
                            null,
                            null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor_tv) {
            super.onPostExecute(cursor_tv);
            list_tv = cursor_tv;
            favTvAdapter.setTvList(list_tv);
            favTvAdapter.notifyDataSetChanged();
        }
    }
}
