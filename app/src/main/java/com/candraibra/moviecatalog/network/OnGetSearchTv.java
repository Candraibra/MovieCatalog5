package com.candraibra.moviecatalog.network;

import com.candraibra.moviecatalog.model.Tv;

import java.util.ArrayList;

public interface OnGetSearchTv {
    void onSuccess(ArrayList<Tv> tvs);

    void onError();
}



