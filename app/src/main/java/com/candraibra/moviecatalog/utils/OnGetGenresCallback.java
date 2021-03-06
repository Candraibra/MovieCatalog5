package com.candraibra.moviecatalog.utils;

import com.candraibra.moviecatalog.model.Genre;

import java.util.List;

public interface OnGetGenresCallback {

    void onSuccess(List<Genre> genres);

    void onError();
}