package com.candraibra.moviecatalog.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.database.DbContract;
import com.candraibra.moviecatalog.database.MovieHelper;
import com.candraibra.moviecatalog.model.Genre;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.network.MoviesRepository;
import com.candraibra.moviecatalog.network.OnGetDetailMovie;
import com.candraibra.moviecatalog.network.OnGetGenresCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MOVIE = "extra_movie";

    public int movieId;
    String id, title, image;
    Movie selectedMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
    private ProgressBar progressBar;
    private ImageView imgBanner, imgPoster;
    private String banner, poster, voteCount;
    private TextView tvTitle, tvOverview, tvRealise, tvGenre, tvRating, tvVoter, tvRealiseYear, tvTitleDesc;
    private MoviesRepository moviesRepository;
    private MovieHelper movieHelper;
    private FloatingActionButton btnFav;

    private boolean isRecordExists(String id) {
        String selection = " movie_id = ?";
        String[] selectionArgs = {id};
        String[] projection = {DbContract.FavoriteMovie.COLUMN_MOVIEID};
        Uri uri = DbContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        Cursor cursor = getContentResolver().query(uri, projection,
                selection, selectionArgs, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        Log.v("isi", Boolean.toString(exists));
        return exists;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = selectedMovie.getId().toString();
        title = selectedMovie.getTitle();
        image = selectedMovie.getBackdropPath();
        movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();
        setContentView(R.layout.activity_detail);
        ImageButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(this);
        btnFav = findViewById(R.id.btnFav);
        btnFav.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        //  if (movieHelper.checkMovie(idMovie)) {
        //      btnFav.setVisibility(View.GONE);
        //      btnDel.setVisibility(View.VISIBLE);
        //  }
        if (isRecordExists(selectedMovie.getId().toString())) {
            if (btnFav != null) {
                btnFav.setImageResource(R.drawable.ic_favorite);
                Log.v("MovieDetail", "" + selectedMovie.getId());
            }
        }
        if (btnFav != null) {
            btnFav.setOnClickListener(v -> {
                if (!isRecordExists(selectedMovie.getId().toString())) {
                    ContentValues contentValues = new ContentValues();
                    // Put the task description and selected mPriority into the ContentValues
                    contentValues.put(DbContract.FavoriteMovie.COLUMN_MOVIEID, id);
                    contentValues.put(DbContract.FavoriteMovie.COLUMN_TITLE, title);
                    contentValues.put(DbContract.FavoriteMovie.COLUMN_POSTER_PATH, image);
                    // Insert the content values via a ContentResolver
                    getContentResolver().insert(DbContract.CONTENT_URI, contentValues);
                    btnFav.setImageResource(R.drawable.ic_favorite);
                    Snackbar.make(v, "This movie has been add to your favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Uri uri = DbContract.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(id).build();
                    Log.v("MovieDetail", "" + uri);

                    getContentResolver().delete(uri, null, null);
                    btnFav.setImageResource(R.drawable.ic_favorite_border);
                    Log.v("MovieDetail", uri.toString());
                    Snackbar.make(v, "This movie has been remove from your favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        getMovie();

    }

    private void getMovie() {
        Movie selectedMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        movieId = selectedMovie.getId();
        moviesRepository = MoviesRepository.getInstance();
        String reviewer = getString(R.string.reviewer);
        moviesRepository.getMovie(movieId, new OnGetDetailMovie() {
            @Override
            public void onSuccess(Movie movie) {
                getGenres(movie);
                poster = movie.getPosterPath();
                tvGenre = findViewById(R.id.tv_genre_text);
                imgBanner = findViewById(R.id.img_poster);
                imgPoster = findViewById(R.id.img_poster2);
                tvTitle = findViewById(R.id.tv_title);
                tvTitleDesc = findViewById(R.id.tv_title_text);
                tvTitleDesc.setText(movie.getTitle());
                banner = movie.getBackdropPath();
                tvTitle.setText(movie.getTitle());
                tvOverview = findViewById(R.id.tv_overview_text);
                tvOverview.setText(movie.getOverview());
                tvRating = findViewById(R.id.tv_rating);
                tvRating.setText(String.valueOf(movie.getVoteAverage()));
                tvVoter = findViewById(R.id.tv_voter);
                voteCount = Integer.toString(movie.getVoteCount());
                tvVoter.setText(voteCount + " " + reviewer);
                tvRealise = findViewById(R.id.tv_realease_text);
                tvRealiseYear = findViewById(R.id.tv_realease_year);
                tvRealiseYear.setText(movie.getReleaseDate().split("-")[0]);
                tvRealise.setText(movie.getReleaseDate());

                if (!isFinishing()) {
                    Picasso.get().load(poster).placeholder(R.drawable.load).into(imgPoster);
                    Picasso.get().load(banner).placeholder(R.drawable.load).into(imgBanner);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    tvGenre.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }


    private void showError() {
        Toast.makeText(DetailMovieActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            onBackPressed();
            {
                finish();
            }
        }
    }


}