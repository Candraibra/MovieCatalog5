package com.candraibra.moviecatalog.utils;

import android.database.Cursor;

import com.candraibra.moviecatalog.model.Movie;

import java.util.ArrayList;

public interface LoadMovieCallback {
    void preExecute();

    void postExecute(Cursor cursor);
}
