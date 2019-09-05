package com.candraibra.moviecatalog.utils;

import com.candraibra.moviecatalog.model.Movie;

import java.util.ArrayList;

public interface LoadMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movies);
}
