package com.candraibra.moviecatalog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIE =
            String.format("CREATE TABLE %s" + "(%s INTEGER PRIMARY KEY AUTOINCREMENT," + " %s TEXT NOT NULL," + " %s TEXT NOT NULL)",
                    DbContract.FavoriteMovie.TABLE_MOVIE,
                    DbContract.FavoriteMovie._ID,
                    DbContract.FavoriteMovie.COLUMN_TITLE,
                    DbContract.FavoriteMovie.COLUMN_POSTER_PATH
            );
    private static final String SQL_CREATE_TABLE_TV =
            String.format("CREATE TABLE %s" + "(%s INTEGER PRIMARY KEY AUTOINCREMENT," + " %s TEXT NOT NULL," + " %s TEXT NOT NULL)",
                    DbContract.FavoriteTv.TABLE_TV,
                    DbContract.FavoriteTv._ID,
                    DbContract.FavoriteTv.COLUMN_TITLE,
                    DbContract.FavoriteMovie.COLUMN_POSTER_PATH

            );
    private static String DATABASE_NAME = "dbmoviecatalog";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.FavoriteMovie.TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.FavoriteTv.TABLE_TV);
        onCreate(db);
    }
}
