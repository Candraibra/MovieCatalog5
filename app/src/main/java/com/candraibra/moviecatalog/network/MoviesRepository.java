package com.candraibra.moviecatalog.network;

import androidx.annotation.NonNull;

import com.candraibra.moviecatalog.BuildConfig;
import com.candraibra.moviecatalog.model.GenreResponse;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static MoviesRepository repository;
    private TMDbApi api;

    private MoviesRepository(TMDbApi api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }

    public void getMoviesPopular(final OnGetMoviesCallback callback) {
        String apiKey = BuildConfig.ApiKey;
        api.getPopularMovies(apiKey, LANGUAGE, 1)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }


    public void getSearchMovie(String query, final OnGetSearchMovie callback) {
        String apiKey = BuildConfig.ApiKey;
        api.getSearchMovie(query, apiKey)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
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

    public void getMovie(int movieId, final OnGetDetailMovie callback) {
        api.getMovie(movieId, BuildConfig.ApiKey, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMoviesPage(int page, final OnGetPageMovie callback) {
        api.getUpcomingMovies(BuildConfig.ApiKey, LANGUAGE, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getRelease(String release, String release1, final OnGetReleaseMovie callback) {
        String apiKey = BuildConfig.ApiKey;
        api.getRealiseMovies(apiKey, release, release1)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }
}
