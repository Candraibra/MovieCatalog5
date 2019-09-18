package com.candraibra.moviecatalog.network;

import com.candraibra.moviecatalog.model.Movie;

import java.util.ArrayList;

public interface OnGetSearchMovie {
    void onSuccess(ArrayList<Movie> movies);

    void onError();
}



