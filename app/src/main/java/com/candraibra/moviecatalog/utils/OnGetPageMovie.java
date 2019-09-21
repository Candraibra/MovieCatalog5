package com.candraibra.moviecatalog.utils;

import com.candraibra.moviecatalog.model.Movie;

import java.util.ArrayList;

public interface OnGetPageMovie {
    void onSuccess(int page, ArrayList<Movie> movies);

    void onError();
}



