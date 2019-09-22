package com.candraibra.moviecatalog.utils;

import com.candraibra.moviecatalog.model.Movie;

import java.util.ArrayList;

public interface OnGetReleaseMovie {
    void onSuccess( ArrayList<Movie> movies);

    void onError();
}


