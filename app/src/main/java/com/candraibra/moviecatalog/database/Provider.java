package com.candraibra.moviecatalog.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.provider.ContactsContract.AUTHORITY;
import static com.candraibra.moviecatalog.database.DbContract.CONTENTTV_URI;
import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI;

public class Provider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;
    private static final int TV = 102;
    private static final int TV_ID = 103;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        // content://com.candraibra.moviecatalog/movie
        sUriMatcher.addURI(AUTHORITY,
                DbContract.FavoriteMovie.TABLE_MOVIE, MOVIE);

        // content://com.candraibra.moviecatalog/movie/id
        sUriMatcher.addURI(AUTHORITY,
                DbContract.FavoriteMovie.TABLE_MOVIE + "/#",
                MOVIE_ID);

        // content://com.candraibra.moviecatalog/tv
        sUriMatcher.addURI(AUTHORITY,
                DbContract.FavoriteTv.TABLE_TV, TV);

        // content://com.candraibra.moviecatalog/tv/id
        sUriMatcher.addURI(AUTHORITY,
                DbContract.FavoriteTv.TABLE_TV + "/#",
                TV_ID);
    }

    private MovieHelper movieHelper;
    private TvHelper tvHelper;

    @Override
    public boolean onCreate() {
        movieHelper = new MovieHelper(getContext());
        movieHelper.open();
        tvHelper = new TvHelper(getContext());
        tvHelper.open();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        Log.v("MovieDetail", "" + match);
        Log.v("MovieDetail", "" + uri);
        Log.v("MovieDetail", "" + uri.getLastPathSegment());
        switch (match) {
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV:
                cursor = tvHelper.queryProvider();
                break;
            case TV_ID:
                cursor = tvHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long added;
        Uri uriContent = null;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(values);
                uriContent = Uri.parse(CONTENT_URI + "/" + added);
                break;
            case TV:
                added = tvHelper.insertProvider(values);
                uriContent = Uri.parse(CONTENTTV_URI + "/" + added);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uriContent;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int movieDeleted;

        Log.v("MovieDetail1", "" + uri);
        int match = sUriMatcher.match(uri);
        Log.v("MovieDetail1", "" + match);
        switch (match) {
            case MOVIE_ID:
                movieDeleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                Log.v("MovieDetail1", "" + movieDeleted);
                break;
            case TV_ID:
                movieDeleted = tvHelper.deleteProvider(uri.getLastPathSegment());
                Log.v("MovieDetail1", "" + movieDeleted);
                break;
            default:
                movieDeleted = 0;
                break;
        }

        if (movieDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int movieUpdated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                movieUpdated = movieHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            case TV_ID:
                movieUpdated = tvHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                movieUpdated = 0;
                break;
        }

        if (movieUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieUpdated;
    }
}
