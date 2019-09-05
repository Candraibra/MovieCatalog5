package com.candraibra.moviecatalog.activity;

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
import com.candraibra.moviecatalog.db.MovieHelper;
import com.candraibra.moviecatalog.model.Genre;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.network.MoviesRepository;
import com.candraibra.moviecatalog.network.OnGetDetailMovie;
import com.candraibra.moviecatalog.network.OnGetGenresCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MOVIE = "extra_movie";

    public int movieId;
    private ProgressBar progressBar;
    private ImageView imgBanner, imgPoster;
    private String banner, poster, voteCount;
    private TextView tvTitle, tvOverview, tvRealise, tvGenre, tvRating, tvVoter, tvRealiseYear, tvTitleDesc;
    private MoviesRepository moviesRepository;
    private MovieHelper movieHelper;
    private ImageButton btnFav, btnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Movie selectedMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        String idMovie = Integer.toString(selectedMovie.getId());
        movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();
        setContentView(R.layout.activity_detail);
        ImageButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(this);
        btnFav = findViewById(R.id.btnFav);
        btnFav.setOnClickListener(this);
        btnDel = findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        if (movieHelper.checkMovie(idMovie)) {
            btnFav.setVisibility(View.GONE);
            btnDel.setVisibility(View.VISIBLE);
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
        } else if (v.getId() == R.id.btnFav) {
            Movie selectedMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            String toastFav = getString(R.string.toastFav);
            String toastFavFail = getString(R.string.toastFavFail);
            long result = movieHelper.insertMovie(selectedMovie);
            if (result > 0) {
                btnFav.setVisibility(View.GONE);
                btnDel.setVisibility(View.VISIBLE);
                Toast.makeText(DetailMovieActivity.this, toastFav, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetailMovieActivity.this, toastFavFail, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnDel) {
            Movie selectedMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            String toastDel = getString(R.string.toastDel);
            movieHelper.deleteMovie(selectedMovie.getId());
            Toast.makeText(DetailMovieActivity.this, toastDel, Toast.LENGTH_SHORT).show();
            btnFav.setVisibility(View.VISIBLE);
            btnDel.setVisibility(View.GONE);
        }
    }
}

