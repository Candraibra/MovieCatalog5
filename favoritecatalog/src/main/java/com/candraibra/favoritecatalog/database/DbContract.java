package com.candraibra.favoritecatalog.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.candraibra.favoritecatalog.database.DbContract.FavoriteMovie.TABLE_MOVIE;
import static com.candraibra.favoritecatalog.database.DbContract.FavoriteTv.TABLE_TV;

public class DbContract {
    static final String AUTHORITY = "com.candraibra.moviecatalog";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_TV)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }


    public static final class FavoriteMovie implements BaseColumns {

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        static final String TABLE_MOVIE = "favorite_movie";

    }

    public static final class FavoriteTv implements BaseColumns {

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        static final String TABLE_TV = "favorite_tv";


    }
}
