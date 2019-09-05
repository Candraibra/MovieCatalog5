package com.candraibra.moviecatalog.network;

import com.candraibra.moviecatalog.model.Tv;

public interface OnGetDetailTv {
    void onSuccess(Tv tv);

    void onError();
}
