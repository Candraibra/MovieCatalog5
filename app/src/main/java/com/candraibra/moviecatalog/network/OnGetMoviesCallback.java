package com.candraibra.moviecatalog.network;

import com.candraibra.moviecatalog.model.Movie;

import java.util.ArrayList;


public interface OnGetMoviesCallback {
    void onSuccess(final ArrayList<Movie> movies);

    void onError();
}
