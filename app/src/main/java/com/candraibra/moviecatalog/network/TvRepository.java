package com.candraibra.moviecatalog.network;

import androidx.annotation.NonNull;

import com.candraibra.moviecatalog.BuildConfig;
import com.candraibra.moviecatalog.model.GenreResponse;
import com.candraibra.moviecatalog.model.Tv;
import com.candraibra.moviecatalog.model.TvResponse;
import com.candraibra.moviecatalog.utils.OnGetDetailTv;
import com.candraibra.moviecatalog.utils.OnGetGenresCallback;
import com.candraibra.moviecatalog.utils.OnGetPageTv;
import com.candraibra.moviecatalog.utils.OnGetSearchTv;
import com.candraibra.moviecatalog.utils.OnGetTvCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static TvRepository repository;

    private TMDbApi api;

    private TvRepository(TMDbApi api) {
        this.api = api;
    }

    public static TvRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new TvRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }

    public void getTvPopular(final OnGetTvCallback callback) {
        String apiKey = BuildConfig.ApiKey;
        api.getPopularTv(apiKey, LANGUAGE, 1)
                .enqueue(new Callback<TvResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TvResponse> call, @NonNull Response<TvResponse> response) {
                        if (response.isSuccessful()) {
                            TvResponse TvResponse = response.body();
                            if (TvResponse != null && TvResponse.getTvs() != null) {
                                callback.onSuccess(TvResponse.getTvs());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TvResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres(BuildConfig.ApiKey, LANGUAGE)
                .enqueue(new Callback<GenreResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GenreResponse> call, @NonNull Response<GenreResponse> response) {
                        if (response.isSuccessful()) {
                            GenreResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GenreResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getSearchTv(String query, final OnGetSearchTv callback) {
        String apiKey = BuildConfig.ApiKey;
        api.getSearchTv(query, apiKey)
                .enqueue(new Callback<TvResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TvResponse> call, @NonNull Response<TvResponse> response) {
                        if (response.isSuccessful()) {
                            TvResponse tvsResponse = response.body();
                            if (tvsResponse != null && tvsResponse.getTvs() != null) {
                                callback.onSuccess(tvsResponse.getTvs());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TvResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }


    public void getTv(int tvId, final OnGetDetailTv callback) {
        api.getTv(tvId, BuildConfig.ApiKey, LANGUAGE)
                .enqueue(new Callback<Tv>() {
                    @Override
                    public void onResponse(@NonNull Call<Tv> call, @NonNull Response<Tv> response) {
                        if (response.isSuccessful()) {
                            Tv tv = response.body();
                            if (tv != null) {
                                callback.onSuccess(tv);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Tv> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTvPage(int page, final OnGetPageTv callback) {
        api.getUpcomingTv(BuildConfig.ApiKey, LANGUAGE, page)
                .enqueue(new Callback<TvResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TvResponse> call, @NonNull Response<TvResponse> response) {
                        if (response.isSuccessful()) {
                            TvResponse tvResponse = response.body();
                            if (tvResponse != null && tvResponse.getTvs() != null) {
                                callback.onSuccess(tvResponse.getPage(), tvResponse.getTvs());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TvResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }
}
