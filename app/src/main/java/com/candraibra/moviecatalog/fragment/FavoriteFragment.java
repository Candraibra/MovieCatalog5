package com.candraibra.moviecatalog.fragment;


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
import com.candraibra.moviecatalog.adapter.FavMovieAdapter;
import com.candraibra.moviecatalog.adapter.FavTvAdapter;
import com.candraibra.moviecatalog.database.MovieHelper;
import com.candraibra.moviecatalog.database.TvHelper;

import java.util.Objects;

import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI;
import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI_TV;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    private final static String LIST_STATE_KEY = "STATE";
    private final static String LIST_STATE_KEY2 = "STATE2";
    private Cursor list;
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


        /**  if (savedInstanceState == null) {
         new LoadMoviesAsync(movieHelper, this).execute();
         new LoadTvAsync(tvHelper, this).execute();
         } else {
         final ArrayList<Movie> moviesState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
         assert moviesState != null;
         movieArrayList.addAll(moviesState);
         favMovieAdapter.setMovieList(moviesState);
         ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
         Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
         intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, moviesState.get(position));
         startActivity(intent);
         });
         final ArrayList<Tv> tvState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY2);
         assert tvState != null;
         tvArrayList.addAll(tvState);
         favTvAdapter.setTvList(tvState);
         ItemClickSupport.addTo(rvTv).setOnItemClickListener((recyclerView, position, v) -> {
         Intent intent = new Intent(getActivity(), DetailTvActivity.class);
         intent.putExtra(DetailTvActivity.EXTRA_TV, tvState.get(position));
         startActivity(intent);
         });
         } */
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
        favMovieAdapter.setMovieList(list);
    }

    private void showRecyclerTv() {
        favTvAdapter = new FavTvAdapter(getActivity());
        rvTv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTv.setHasFixedSize(true);
        rvTv.setAdapter(favTvAdapter);
        favTvAdapter.setTvList(list);
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
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            list = cursor;
            favMovieAdapter.setMovieList(list);
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
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            list = cursor;
            favTvAdapter.setTvList(list);
            favTvAdapter.notifyDataSetChanged();
        }
    }
}
