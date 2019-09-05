package com.candraibra.moviecatalog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.candraibra.moviecatalog.model.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static androidx.constraintlayout.motion.widget.MotionScene.TAG;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.TABLE_NAME;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.COLUMN_BACKDROP_PATH;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.COLUMN_MOVIEID;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.COLUMN_OVERVIEW;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.COLUMN_POSTER_PATH;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.COLUMN_REALISE;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.COLUMN_TITLE;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.COLUMN_USERRATING;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.COLUMN_VOTER;
import static com.candraibra.moviecatalog.db.DbContract.FavoriteMovie.TABLE_NAME;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DbHelper dataBaseHelper;
    private static MovieHelper INSTANCE;
    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new DbHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<Movie> getAllMovie() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_MOVIEID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_BACKDROP_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_OVERVIEW)));
                movie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_VOTER))));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_REALISE)));

                arrayList.add(movie);

                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_MOVIEID, movie.getId());
        args.put(COLUMN_TITLE, movie.getTitle());
        args.put(COLUMN_OVERVIEW, movie.getOverview());
        args.put(COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        args.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        args.put(COLUMN_USERRATING, movie.getVoteAverage());
        args.put(COLUMN_VOTER, movie.getVoteCount());
        args.put(COLUMN_REALISE, movie.getReleaseDate());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public void deleteMovie(int id) {
        database = dataBaseHelper.getWritableDatabase();
        database.delete(TABLE_NAME, DbContract.FavoriteMovie.COLUMN_MOVIEID + "=" + id, null);
    }

    public boolean checkMovie(String id) {
        database = dataBaseHelper.getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME + " WHERE " + DbContract.FavoriteMovie.COLUMN_MOVIEID + " =?";
        Cursor cursor = database.rawQuery(selectString, new String[]{id});
        boolean checkMovie = false;
        if (cursor.moveToFirst()) {
            checkMovie = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            Log.d(TAG, String.format("%d records found", count));
        }
        cursor.close();
        return checkMovie;
    }
}

