package com.candraibra.moviecatalog.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.candraibra.moviecatalog.R;

public class SearchMovieActivity extends AppCompatActivity {
    public static String SEARCH_MOVIE = "query";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
