package com.candraibra.moviecatalog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import static com.candraibra.moviecatalog.database.DbContract.FavoriteTv.TABLE_TV;

public class TvHelper {
    private static final String DATABASE_TABLE = TABLE_TV;
    private static DbHelper dataBaseHelper;
    private static TvHelper INSTANCE;
    private static SQLiteDatabase database;

    TvHelper(Context context) {
        dataBaseHelper = new DbHelper(context);
    }

    public static TvHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }


    Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , MediaStore.Audio.Playlists.Members._ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , MediaStore.Audio.Playlists.Members._ID + " ASC");
    }

    long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, MediaStore.Audio.Playlists.Members._ID + " =?", new String[]{id});
    }

    int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, MediaStore.Audio.Playlists.Members._ID + " = ?", new String[]{id});
    }
}
