# Proyek Akhir: Aplikasi Movie Catalogue

##### Home
![Home](demo/home.jpeg)
#####Notification
![Notif](demo/notif.jpeg)
##### Movie Fragment
![Movie](demo/movie.jpeg)
##### Favorite Fragment
![Favorite](demo/favorite.jpeg)
##### Search Movie
![Search](demo/search_movie.jpeg)
#####Widget
![Widget](demo/widget.jpeg)
### Kriteria
- Pengguna dapat melakukan pencarian Movies.
- Pengguna dapat melakukan pencarian Tv Show.
- Pengguna dapat menampilkan widget dari film favorite ke halaman utama smartphone.
- Tipe widget yang diterapkan adalah Stack Widget.
- Daily Reminder, mengirimkan notifikasi ke pengguna untuk kembali ke Aplikasi Movie Catalogue. Daily reminder harus selalu berjalan tiap jam 7 pagi.
- Release Today Reminder, mengirimkan notifikasi ke pengguna berupa informasi film yang rilis hari ini (wajib menggunakan endpoint seperti yang telah disediakan pada bagian Resources di bawah). Release reminder harus selalu berjalan tiap jam 8 pagi.
- Terdapat halaman pengaturan untuk mengaktifkan dan menonaktifkan reminder.
- Membuat aplikasi atau modul baru yang menampilkan daftar film favorite.
- Menggunakan Content Provider sebagai mekanisme untuk mengakses data dari satu aplikasi ke aplikasi lain.

##### Kali Ini Kami Diharuskan Menggunakan Content Provider Pada Awalnya Saya Stack Lama Karena Belum Paham Betul Konsep Content Provider Namun Setelah Saya Menari Cari Referensi Di Stack Overflow Dan Internet Seperti Ini Contohnya

------------
`DbContract.Java`
```java
package com.candraibra.moviecatalog.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.candraibra.moviecatalog.database.DbContract.FavoriteMovie.TABLE_MOVIE;
import static com.candraibra.moviecatalog.database.DbContract.FavoriteTv.TABLE_TV;

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

```
`MovieHelper.Java`
```java
package com.candraibra.moviecatalog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import static com.candraibra.moviecatalog.database.DbContract.FavoriteMovie.TABLE_MOVIE;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DbHelper dataBaseHelper;
    private static MovieHelper INSTANCE;
    private static SQLiteDatabase database;

    MovieHelper(Context context) {
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

```
`Provider`
```java
package com.candraibra.moviecatalog.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import static com.candraibra.moviecatalog.database.DbContract.AUTHORITY;
import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI;
import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI_TV;
import static com.candraibra.moviecatalog.database.DbContract.FavoriteMovie.TABLE_MOVIE;
import static com.candraibra.moviecatalog.database.DbContract.FavoriteTv.TABLE_TV;

public class Provider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    private static final int TV = 200;
    private static final int TV_ID = 201;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);

        sUriMatcher.addURI(AUTHORITY,
                TABLE_MOVIE + "/#",
                MOVIE_ID);
    }

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_TV, TV);

        sUriMatcher.addURI(AUTHORITY,
                TABLE_TV + "/#",
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
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
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
            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long added;
        Uri contentUri = null;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(contentValues);
                if (added > 0) {
                    contentUri = ContentUris.withAppendedId(CONTENT_URI, added);
                }
                break;
            case TV:
                added = tvHelper.insertProvider(contentValues);
                if (added > 0) {
                    contentUri = ContentUris.withAppendedId(CONTENT_URI_TV, added);
                }
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return contentUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            case TV_ID:
                deleted = tvHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated = movieHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            case TV_ID:
                updated = tvHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }
}
```
##### Cara Mengimpplementasikannya
`FavoriteFragment.Java`
```java
package com.candraibra.moviecatalog.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.activity.DetailMovieActivity;
import com.candraibra.moviecatalog.activity.DetailTvActivity;
import com.candraibra.moviecatalog.adapter.FavMovieAdapter;
import com.candraibra.moviecatalog.adapter.FavTvAdapter;
import com.candraibra.moviecatalog.database.MovieHelper;
import com.candraibra.moviecatalog.database.TvHelper;
import com.candraibra.moviecatalog.utils.ItemClickSupport;

import java.util.Objects;

import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI;
import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI_TV;

public class FavoriteFragment extends Fragment {

    private Cursor list_movie;
    private Cursor list_tv;
    private RecyclerView rvMovie, rvTv;
    private FavMovieAdapter favMovieAdapter;
    private FavTvAdapter favTvAdapter;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rv_liked_movie);
        rvTv = view.findViewById(R.id.rv_liked_tv);
        MovieHelper movieHelper = MovieHelper.getInstance(getActivity());
        movieHelper.open();
        TvHelper tvHelper = TvHelper.getInstance(getActivity());
        tvHelper.open();
        new loadMovie().execute();
        showRecyclerMovie();
        new loadTv().execute();
        showRecyclerTv();

    }

    private void showRecyclerMovie() {
        favMovieAdapter = new FavMovieAdapter(getActivity());
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvMovie.setAdapter(favMovieAdapter);
        rvMovie.setHasFixedSize(true);
        favMovieAdapter.setMovieList(list_movie);
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailTvActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, list_movie.getPosition());
            startActivity(intent);
        });
    }

    private void showRecyclerTv() {
        favTvAdapter = new FavTvAdapter(getActivity());
        rvTv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTv.setHasFixedSize(true);
        rvTv.setAdapter(favTvAdapter);
        favTvAdapter.setTvList(list_tv);
        ItemClickSupport.addTo(rvTv).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailTvActivity.class);
            intent.putExtra(DetailTvActivity.EXTRA_TV, list_tv.getPosition());
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class loadMovie extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getContext()).getContentResolver()
                    .query(
                            CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor_movie) {
            super.onPostExecute(cursor_movie);
            list_movie = cursor_movie;
            favMovieAdapter.setMovieList(list_movie);
            favMovieAdapter.notifyDataSetChanged();
        }
    }

    private class loadTv extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getContext()).getContentResolver()
                    .query(
                            CONTENT_URI_TV,
                            null,
                            null,
                            null,
                            null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor_tv) {
            super.onPostExecute(cursor_tv);
            list_tv = cursor_tv;
            favTvAdapter.setTvList(list_tv);
            favTvAdapter.notifyDataSetChanged();
        }
    }
}

```
