package com.candraibra.moviecatalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.adapter.MoviePageAdapter;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.network.MoviesRepository;
import com.candraibra.moviecatalog.network.OnGetSearchMovie;
import com.candraibra.moviecatalog.utils.ItemClickSupport;

import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static String SEARCH_MOVIE = "query";
    RecyclerView rvSearch;
    MoviePageAdapter adapter;
    MoviesRepository moviesRepository;
    ProgressBar progressBar;
    TextView title, btnBack;
    String query;
    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        rvSearch = findViewById(R.id.rv_search_movie);
        progressBar = findViewById(R.id.progressBar);
        moviesRepository = MoviesRepository.getInstance();
        btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(this);
        title = findViewById(R.id.tv_title);

        Intent intent = getIntent();
        query = intent.getStringExtra(SEARCH_MOVIE);

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
                progressBar.setVisibility(View.GONE);
                adapter.setMovieList(movies);
                rvSearch.setAdapter(adapter);
                title.setText(query);
                ItemClickSupport.addTo(rvSearch).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(SearchMovieActivity.this, DetailMovieActivity.class);
                    intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies.get(position));
                    startActivity(intent);
                });
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.toastmsg);
                Toast.makeText(SearchMovieActivity.this, toast_msg, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
            onBackPressed();
            {
                finish();
            }
        }
    }
}
