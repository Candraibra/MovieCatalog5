package com.candraibra.moviecatalog.utils;

import com.candraibra.moviecatalog.model.Tv;

import java.util.ArrayList;

public interface LoadTvCallback {
    void preExecute();

    void postExecute2(ArrayList<Tv> tvs);
}
