package com.candraibra.favoritecatalog.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.candraibra.favoritecatalog.database.DbContract.FavoriteMovie.COLUMN_POSTER_PATH;
import static com.candraibra.favoritecatalog.database.DbContract.FavoriteMovie.COLUMN_TITLE;
import static com.candraibra.favoritecatalog.database.DbContract.getColumnInt;
import static com.candraibra.favoritecatalog.database.DbContract.getColumnString;

public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;

    public Movie() {
    }

    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, COLUMN_TITLE);
        this.posterPath = getColumnString(cursor, COLUMN_POSTER_PATH);
    }

    public Movie(Parcel in) {
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.title = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.posterPath = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.backdropPath = in.readString();
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Double getVoteAverage() {
        return voteAverage;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w342" + posterPath;
    }


    public String getPosterPathFav() {
        return posterPath;
    }

    public String getPosterPathMini() {
        return "https://image.tmdb.org/t/p/w154" + posterPath;
    }


    public String getBackdropPath() {
        return "https://image.tmdb.org/t/p/w780" + backdropPath;
    }


    public String getOverview() {
        return overview;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.voteCount);
        dest.writeValue(this.id);
        dest.writeValue(this.video);
        dest.writeValue(this.voteAverage);
        dest.writeString(this.title);
        dest.writeValue(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeList(this.genreIds);
        dest.writeString(this.backdropPath);
        dest.writeValue(this.adult);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }
}