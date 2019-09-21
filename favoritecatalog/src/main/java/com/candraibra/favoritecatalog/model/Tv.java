package com.candraibra.favoritecatalog.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.candraibra.favoritecatalog.database.DbContract.FavoriteTv.COLUMN_POSTER_PATH;
import static com.candraibra.favoritecatalog.database.DbContract.FavoriteTv.COLUMN_TITLE;
import static com.candraibra.favoritecatalog.database.DbContract.getColumnInt;
import static com.candraibra.favoritecatalog.database.DbContract.getColumnString;

public class Tv implements Parcelable {

    public static final Creator<Tv> CREATOR = new Creator<Tv>() {
        @Override
        public Tv createFromParcel(Parcel source) {
            return new Tv(source);
        }

        @Override
        public Tv[] newArray(int size) {
            return new Tv[size];
        }
    };
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = null;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;

    public Tv() {
    }


    public Tv(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.name = getColumnString(cursor, COLUMN_TITLE);
        this.posterPath = getColumnString(cursor, COLUMN_POSTER_PATH);
    }

    public Tv(Parcel in) {
        this.originalName = in.readString();
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.name = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.originCountry = in.createStringArrayList();
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstAirDate = in.readString();
        this.backdropPath = in.readString();
        this.originalLanguage = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.overview = in.readString();
        this.posterPath = in.readString();
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getVoteCount() {
        return voteCount;
    }


    public String getFirstAirDate() {
        return firstAirDate;
    }


    public String getBackdropPath() {
        return "https://image.tmdb.org/t/p/w780" + backdropPath;
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


    public String getOverview() {
        return overview;
    }


    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w342" + posterPath;
    }


    public String getPosterPathMini() {
        return "https://image.tmdb.org/t/p/w154" + posterPath;
    }

    public String getPosterPathFav() {
        return posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalName);
        dest.writeList(this.genreIds);
        dest.writeString(this.name);
        dest.writeValue(this.popularity);
        dest.writeStringList(this.originCountry);
        dest.writeValue(this.voteCount);
        dest.writeString(this.firstAirDate);
        dest.writeString(this.backdropPath);
        dest.writeString(this.originalLanguage);
        dest.writeValue(this.id);
        dest.writeValue(this.voteAverage);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
    }
}