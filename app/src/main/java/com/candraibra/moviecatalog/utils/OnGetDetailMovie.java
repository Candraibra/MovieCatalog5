package com.candraibra.moviecatalog.utils;

import com.candraibra.moviecatalog.model.Movie;

public interface OnGetDetailMovie {
    void onSuccess(Movie movie);

    void onError();
}
