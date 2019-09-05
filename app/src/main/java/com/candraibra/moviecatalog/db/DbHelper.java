package com.candraibra.moviecatalog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbmoviecatalog";

    private static final int DATABASE_VERSION = 1;
    DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String SQL_CREATE_TABLE_MOVIE =
            String.format("CREATE TABLE %s" +
                            "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s INTEGER," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s INTEGER," +
                            " %s TEXT NOT NULL)",
                    DbContract.FavoriteMovie.TABLE_NAME,
                    DbContract.FavoriteMovie._ID,
                    DbContract.FavoriteMovie.COLUMN_MOVIEID,
                    DbContract.FavoriteMovie.COLUMN_TITLE,
                    DbContract.FavoriteMovie.COLUMN_REALISE,
                    DbContract.FavoriteMovie.COLUMN_BACKDROP_PATH,
                    DbContract.FavoriteMovie.COLUMN_POSTER_PATH,
                    DbContract.FavoriteMovie.COLUMN_USERRATING,
                    DbContract.FavoriteMovie.COLUMN_VOTER,
                    DbContract.FavoriteMovie.COLUMN_OVERVIEW
            );
    private static final String SQL_CREATE_TABLE_TV =
            String.format("CREATE TABLE %s" +
                            "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s INTEGER," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s INTEGER," +
                            " %s TEXT NOT NULL)",
                    DbContract.FavoriteTv.TABLE_NAME,
                    DbContract.FavoriteTv._ID,
                    DbContract.FavoriteTv.COLUMN_MOVIEID,
                    DbContract.FavoriteTv.COLUMN_TITLE,
                    DbContract.FavoriteTv.COLUMN_FIRST_REALISE,
                    DbContract.FavoriteTv.COLUMN_BACKDROP_PATH,
                    DbContract.FavoriteTv.COLUMN_POSTER_PATH,
                    DbContract.FavoriteTv.COLUMN_USERRATING,
                    DbContract.FavoriteTv.COLUMN_VOTER,
                    DbContract.FavoriteTv.COLUMN_OVERVIEW
            );
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.FavoriteMovie.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.FavoriteTv.TABLE_NAME);
        onCreate(db);
    }
}
