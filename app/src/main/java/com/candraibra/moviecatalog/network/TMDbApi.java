package com.candraibra.moviecatalog.network;

import com.candraibra.moviecatalog.model.GenreResponse;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.model.MoviesResponse;
import com.candraibra.moviecatalog.model.Tv;
import com.candraibra.moviecatalog.model.TvResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {
    //popular_movie
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    //upcoming_movie
    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    //search_movie
    @GET("search/movie")
    Call<MoviesResponse> getSearchMovie(
            @Query("query") String query,
            @Query("api_key") String apiKey
    );

    //search_tv
    @GET("search/tv")
    Call<TvResponse> getSearchTv(
            @Query("query") String query,
            @Query("api_key") String apiKey
    );

    //popular_tv
    @GET("tv/popular")
    Call<TvResponse> getPopularTv(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    //on_the_air_tv
    @GET("tv/on_the_air")
    Call<TvResponse> getUpcomingTv(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    //genre_movies
    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    //get_movie_by_id
    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    //get_tv_by_id
    @GET("tv/{tv_id}")
    Call<Tv> getTv(
            @Path("tv_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

}