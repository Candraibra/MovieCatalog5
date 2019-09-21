package com.candraibra.moviecatalog.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.database.MovieHelper;
import com.candraibra.moviecatalog.model.Genre;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.network.MoviesRepository;
import com.candraibra.moviecatalog.utils.OnGetDetailMovie;
import com.candraibra.moviecatalog.utils.OnGetGenresCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI;
import static com.candraibra.moviecatalog.database.DbContract.FavoriteMovie.COLUMN_POSTER_PATH;
import static com.candraibra.moviecatalog.database.DbContract.FavoriteMovie.COLUMN_TITLE;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MOVIE = "extra_movie";
    Integer movieId;
    private boolean isAdd = false;
    private Movie selectedMovie;
    private ProgressBar progressBar;
    private ImageView imgBanner, imgPoster;
    private String banner, poster, voteCount;
    private TextView tvTitle, tvOverview, tvRealise, tvGenre, tvRating, tvVoter, tvRealiseYear, tvTitleDesc;
    private MoviesRepository moviesRepository;
    private FloatingActionButton btnFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        selectedMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        ImageButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(this);

        btnFav = findViewById(R.id.btnFav);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        btnFav.setOnClickListener(v -> {
            if (isAdd) {
                removeFavorite();
            } else {
                addFavorite();
            }
            isAdd = !isAdd;
            if (isAdd) btnFav.setImageResource(R.drawable.ic_favorite);
            else btnFav.setImageResource(R.drawable.ic_favorite_border);
        });

        loadMovie();
        getMovie();

    }

    private void loadMovie() {
        MovieHelper movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();
        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + selectedMovie.getId()),
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) isAdd = true;
            cursor.close();
        }

        if (isAdd) btnFav.setImageResource(R.drawable.ic_favorite);
        else btnFav.setImageResource(R.drawable.ic_favorite_border);
    }

    private void addFavorite() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(_ID, selectedMovie.getId());
        contentValues.put(COLUMN_TITLE, selectedMovie.getTitle());
        contentValues.put(COLUMN_POSTER_PATH, selectedMovie.getPosterPath());

        getContentResolver().insert(CONTENT_URI, contentValues);

        Toast.makeText(this, R.string.toastFav, Toast.LENGTH_LONG).show();
    }

    private void removeFavorite() {

        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + selectedMovie.getId()),
                null,
                null
        );
        Toast.makeText(this, R.string.toastDel, Toast.LENGTH_LONG).show();
    }


    private void getMovie() {
        movieId = selectedMovie.getId();
        String reviewer = getString(R.string.reviewer);
        moviesRepository = MoviesRepository.getInstance();
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
                    Picasso.get().load(banner).placeholder(R.drawable.load2).into(imgBanner);
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