package com.candraibra.moviecatalog.utils;

import com.candraibra.moviecatalog.model.Tv;

import java.util.ArrayList;

public interface OnGetTvCallback {
    void onSuccess(final ArrayList<Tv> tvs);

    void onError();
}
