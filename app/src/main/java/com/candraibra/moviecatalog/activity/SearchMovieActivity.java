package com.candraibra.moviecatalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.adapter.MovieAdapter;
import com.candraibra.moviecatalog.adapter.MoviePageAdapter;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.network.MoviesRepository;
import com.candraibra.moviecatalog.network.OnGetSearchMovie;
import com.candraibra.moviecatalog.network.TMDbApi;

import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {
    public static String SEARCH_MOVIE = "query";
    RecyclerView rvSearch;
    MoviePageAdapter adapter;
    MoviesRepository moviesRepository;
    ProgressBar progressBar;
    ArrayList<Movie> movies = new ArrayList<>();
    TMDbApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        rvSearch = findViewById(R.id.rv_search_movie);
        progressBar = findViewById(R.id.progressBar);
        moviesRepository = MoviesRepository.getInstance();

        Intent intent = getIntent();
        String query =  intent.getStringExtra(SEARCH_MOVIE);

        showRecycler();
        getSearchMovie(query);
    }

    private void showRecycler() {
        adapter = new MoviePageAdapter(this);
        rvSearch.setLayoutManager(new GridLayoutManager(this, 3));
        rvSearch.setHasFixedSize(true);

    }

    private void getSearchMovie(String query) {
        moviesRepository.getSearchMovie(query, new OnGetSearchMovie() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                adapter.setMovieList(movies);
                rvSearch.setAdapter(adapter);
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.toastmsg);
                Toast.makeText(SearchMovieActivity.this, toast_msg, Toast.LENGTH_SHORT).show();
            }

        });
    }

}
