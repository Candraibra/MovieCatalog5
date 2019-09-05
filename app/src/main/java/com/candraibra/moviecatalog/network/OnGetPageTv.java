package com.candraibra.moviecatalog.network;

import com.candraibra.moviecatalog.model.Tv;

import java.util.ArrayList;

public interface OnGetPageTv {
    void onSuccess(int page, ArrayList<Tv> tvs);

    void onError();
}



