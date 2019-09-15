package com.candraibra.moviecatalog.database;

import android.database.Cursor;

import com.candraibra.moviecatalog.model.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class ProviderHelper {
    public static ArrayList<Movie> movieCursorToArrayList(Cursor movieCursor) {
        ArrayList<Movie> movieList = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(_ID));
            String title = movieCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String description = movieCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION));
            String date = movieCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));
            movieList.add(new Movie(id, title, description, date));
        }

        return notesList;
    }
}
