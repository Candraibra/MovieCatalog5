package com.candraibra.moviecatalog.fragment;


import android.content.Intent;
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
import com.candraibra.moviecatalog.adapter.FavAdapter;
import com.candraibra.moviecatalog.adapter.FavTvAdapter;
import com.candraibra.moviecatalog.db.MovieHelper;
import com.candraibra.moviecatalog.db.TvHelper;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.model.Tv;
import com.candraibra.moviecatalog.utils.ItemClickSupport;
import com.candraibra.moviecatalog.utils.LoadMovieCallback;
import com.candraibra.moviecatalog.utils.LoadTvCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoadMovieCallback, LoadTvCallback {
    private RecyclerView rvMovie, rvTv;
    private FavAdapter favAdapter;
    private FavTvAdapter favTvAdapter;
    private final static String LIST_STATE_KEY = "STATE";
    private final static String LIST_STATE_KEY2 = "STATE2";
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private ArrayList<Tv> tvArrayList = new ArrayList<>();

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rv_liked_movie);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvMovie.setHasFixedSize(true);

        rvTv = view.findViewById(R.id.rv_liked_tv);
        rvTv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTv.setHasFixedSize(true);

        MovieHelper movieHelper = MovieHelper.getInstance(getActivity());
        movieHelper.open();

        TvHelper tvHelper = TvHelper.getInstance(getActivity());
        tvHelper.open();

        favAdapter = new FavAdapter(getActivity());
        rvMovie.setAdapter(favAdapter);

        favTvAdapter = new FavTvAdapter(getActivity());
        rvTv.setAdapter(favTvAdapter);

        if (savedInstanceState == null) {
            new LoadMoviesAsync(movieHelper, this).execute();
            new LoadTvAsync(tvHelper, this).execute();
        } else {
            final ArrayList<Movie> moviesState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert moviesState != null;
            movieArrayList.addAll(moviesState);
            favAdapter.setMovieList(moviesState);
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
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, movieArrayList);
        outState.putParcelableArrayList(LIST_STATE_KEY2, tvArrayList);
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute2(ArrayList<Tv> tvs) {
        favTvAdapter.setTvList(tvs);
        rvTv.setAdapter(favTvAdapter);
        tvArrayList.addAll(tvs);
        ItemClickSupport.addTo(rvTv).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailTvActivity.class);
            intent.putExtra(DetailTvActivity.EXTRA_TV, tvs.get(position));
            startActivity(intent);
        });
    }


    @Override
    public void postExecute(ArrayList<Movie> movies) {
        favAdapter.setMovieList(movies);
        rvMovie.setAdapter(favAdapter);
        movieArrayList.addAll(movies);
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies.get(position));
            startActivity(intent);
        });
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<MovieHelper> weakMovieHelper;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMoviesAsync(MovieHelper movieHelper, LoadMovieCallback callback) {
            weakMovieHelper = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            return weakMovieHelper.get().getAllMovie();
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    private static class LoadTvAsync extends AsyncTask<Void, Void, ArrayList<Tv>> {
        private final WeakReference<TvHelper> weakTvHelper;
        private final WeakReference<LoadTvCallback> weakCallback;

        private LoadTvAsync(TvHelper tvHelper, LoadTvCallback callback) {
            weakTvHelper = new WeakReference<>(tvHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Tv> doInBackground(Void... voids) {
            return weakTvHelper.get().getAllTv();
        }


        @Override
        protected void onPostExecute(ArrayList<Tv> tvs) {
            super.onPostExecute(tvs);
            weakCallback.get().postExecute2(tvs);
        }
    }

}
