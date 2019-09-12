package com.candraibra.moviecatalog.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.candraibra.moviecatalog.database.DbContract.FavoriteMovie.TABLE_MOVIE;
import static com.candraibra.moviecatalog.database.DbContract.FavoriteTv.TABLE_TV;
public class DbContract {

    private static final String AUTHORITY = "com.candraibra.moviecatalog";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();
    public static final Uri CONTENTTV_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_TV)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static final class FavoriteMovie implements BaseColumns {

        static final String TABLE_MOVIE = "favorite_movie";
       public static final String COLUMN_MOVIEID = "movieid";
        public static final String COLUMN_TITLE = "title";
        static final String COLUMN_USERRATING = "userrating";
        static final String COLUMN_BACKDROP_PATH = "backdroppath";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_VOTER = "voter";
        static final String COLUMN_REALISE = "realise";

    }

   public static final class FavoriteTv implements BaseColumns {

        static final String TABLE_TV = " tv";
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
