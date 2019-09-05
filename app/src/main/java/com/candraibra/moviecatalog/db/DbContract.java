package com.candraibra.moviecatalog.db;

import android.provider.BaseColumns;

class DbContract {
    static final class FavoriteMovie implements BaseColumns {

        static final String TABLE_NAME = "favorite_movie";
        static final String COLUMN_MOVIEID = "movieid";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_USERRATING = "userrating";
        static final String COLUMN_BACKDROP_PATH = "backdroppath";
        static final String COLUMN_POSTER_PATH = "posterpath";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_VOTER = "voter";
        static final String COLUMN_REALISE = "realise";

    }
    static final class FavoriteTv implements BaseColumns {

        static final String TABLE_NAME = " tv";
        static final String COLUMN_MOVIEID = "tv_id";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_USERRATING = "userrating";
        static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        static final String COLUMN_POSTER_PATH = "poster_path";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_VOTER = "voter";
        static final String COLUMN_FIRST_REALISE = "realise";

    }
}
