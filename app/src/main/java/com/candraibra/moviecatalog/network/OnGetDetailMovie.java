package com.candraibra.moviecatalog.network;

import com.candraibra.moviecatalog.model.Movie;

public interface OnGetDetailMovie {
    void onSuccess(Movie movie);

    void onError();
}
