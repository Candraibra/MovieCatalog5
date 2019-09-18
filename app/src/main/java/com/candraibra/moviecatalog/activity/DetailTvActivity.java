package com.candraibra.moviecatalog.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.database.TvHelper;
import com.candraibra.moviecatalog.model.Genre;
import com.candraibra.moviecatalog.model.Tv;
import com.candraibra.moviecatalog.network.OnGetDetailTv;
import com.candraibra.moviecatalog.network.OnGetGenresCallback;
import com.candraibra.moviecatalog.network.TvRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI_TV;
import static com.candraibra.moviecatalog.database.DbContract.FavoriteMovie.COLUMN_POSTER_PATH;
import static com.candraibra.moviecatalog.database.DbContract.FavoriteMovie.COLUMN_TITLE;

public class DetailTvActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TV = "extra_tv";
    public int tvId;
    private boolean isAdd = false;
    private Tv selectedTv;
    private ProgressBar progressBar;
    private ImageView imgBanner, imgPoster;
    private String banner, poster, voteCount;
    private TextView tvTitle, tvOverview, tvRealiseFirst, tvGenre, tvRating, tvVoter, tvRealiseYear, tvTitleDesc;
    private TvRepository tvRepository;
    private FloatingActionButton btnFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtv);

        selectedTv = getIntent().getParcelableExtra(EXTRA_TV);

        btnFav = findViewById(R.id.btnFav);
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(this);

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
        loadTv();
        getTv();
    }

    private void loadTv() {
        TvHelper tvHelper = TvHelper.getInstance(getApplicationContext());
        tvHelper.open();
        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI_TV + "/" + selectedTv.getId()),
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

        contentValues.put(_ID, selectedTv.getId());
        contentValues.put(COLUMN_TITLE, selectedTv.getName());
        contentValues.put(COLUMN_POSTER_PATH, selectedTv.getPosterPath());

        getContentResolver().insert(CONTENT_URI_TV, contentValues);

        Toast.makeText(this, R.string.toastFav, Toast.LENGTH_LONG).show();
    }

    private void removeFavorite() {

        getContentResolver().delete(
                Uri.parse(CONTENT_URI_TV + "/" + selectedTv.getId()),
                null,
                null
        );
        Toast.makeText(this, R.string.toastDel, Toast.LENGTH_LONG).show();
    }


    private void getTv() {
        tvId = selectedTv.getId();
        String reviewer = getString(R.string.reviewer);
        tvRepository = TvRepository.getInstance();
        tvRepository.getTv(tvId, new OnGetDetailTv() {
            @Override
            public void onSuccess(Tv tv) {
                getGenres2(tv);
                poster = tv.getPosterPath();
                tvGenre = findViewById(R.id.tv_genre_text);
                imgBanner = findViewById(R.id.img_poster);
                imgPoster = findViewById(R.id.img_poster2);
                tvTitle = findViewById(R.id.tv_title);
                tvTitleDesc = findViewById(R.id.tv_title_text);
                tvTitleDesc.setText(tv.getName());
                banner = tv.getBackdropPath();
                tvTitle.setText(tv.getName());
                tvOverview = findViewById(R.id.tv_overview_text);
                tvOverview.setText(tv.getOverview());
                tvVoter = findViewById(R.id.tv_voter);
                voteCount = Integer.toString(tv.getVoteCount());
                tvVoter.setText(voteCount + " " + reviewer);
                tvRating = findViewById(R.id.tv_rating);
                tvRating.setText(String.valueOf(tv.getVoteAverage()));
                tvRealiseYear = findViewById(R.id.tv_realease_year);
                tvRealiseYear.setText(tv.getFirstAirDate().split("-")[0]);
                tvRealiseFirst = findViewById(R.id.tv_realease_text);
                tvRealiseFirst.setText(tv.getFirstAirDate());

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

    private void getGenres2(final Tv tv) {
        tvRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (tv.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : tv.getGenres()) {
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
        Toast.makeText(DetailTvActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
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

