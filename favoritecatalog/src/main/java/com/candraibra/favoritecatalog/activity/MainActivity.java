package com.candraibra.favoritecatalog.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.favoritecatalog.R;
import com.candraibra.favoritecatalog.adapter.FavMovieAdapter;
import com.candraibra.favoritecatalog.adapter.FavTvAdapter;
import com.candraibra.favoritecatalog.database.MovieHelper;
import com.candraibra.favoritecatalog.database.TvHelper;

import java.util.Objects;

import static com.candraibra.favoritecatalog.database.DbContract.CONTENT_URI;
import static com.candraibra.favoritecatalog.database.DbContract.CONTENT_URI_TV;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvMovie, rvTv;
    FavMovieAdapter favMovieAdapter;
    FavTvAdapter favTvAdapter;
    private Cursor list_movie;
    private Cursor list_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvMovie = findViewById(R.id.rv_liked_movie);
        rvTv = findViewById(R.id.rv_liked_tv);
        MovieHelper movieHelper = MovieHelper.getInstance(this);
        movieHelper.open();
        TvHelper tvHelper = TvHelper.getInstance(this);
        tvHelper.open();
        new loadMovie().execute();
        showRecyclerMovie();
        new loadTv().execute();
        showRecyclerTv();
    }

    private void showRecyclerMovie() {
        favMovieAdapter = new FavMovieAdapter(this);
        rvMovie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvMovie.setAdapter(favMovieAdapter);
        rvMovie.setHasFixedSize(true);
        favMovieAdapter.setMovieList(list_movie);
    }

    private void showRecyclerTv() {
        favTvAdapter = new FavTvAdapter(this);
        rvTv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTv.setHasFixedSize(true);
        rvTv.setAdapter(favTvAdapter);
        favTvAdapter.setTvList(list_tv);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @SuppressLint("StaticFieldLeak")
    private class loadMovie extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getApplicationContext()).getContentResolver()
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

    @SuppressLint("StaticFieldLeak")
    private class loadTv extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getApplicationContext()).getContentResolver()
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
