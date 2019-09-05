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
import com.candraibra.moviecatalog.db.TvHelper;
import com.candraibra.moviecatalog.model.Genre;
import com.candraibra.moviecatalog.model.Tv;
import com.candraibra.moviecatalog.network.OnGetDetailTv;
import com.candraibra.moviecatalog.network.OnGetGenresCallback;
import com.candraibra.moviecatalog.network.TvRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailTvActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TV = "extra_tv";
    public int tvId;
    private ProgressBar progressBar;
    private ImageView imgBanner, imgPoster;
    private String banner, poster, voteCount;
    private TextView tvTitle, tvOverview, tvRealiseFirst, tvGenre, tvRating, tvVoter, tvRealiseYear, tvTitleDesc;
    private TvRepository tvRepository;
    private ImageButton btnFav, btnDel;
    private TvHelper tvHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tv selectedTv = getIntent().getParcelableExtra(EXTRA_TV);
        String idTv = Integer.toString(selectedTv.getId());
        tvHelper = TvHelper.getInstance(getApplicationContext());
        tvHelper.open();
        setContentView(R.layout.activity_detailtv);
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        btnFav = findViewById(R.id.btnFav);
        btnFav.setOnClickListener(this);
        btnDel = findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);
        if (tvHelper.checkTv(idTv)) {
            btnFav.setVisibility(View.GONE);
            btnDel.setVisibility(View.VISIBLE);
        }
        getTv();
    }


    private void getTv() {
        Tv selectedTv = getIntent().getParcelableExtra(EXTRA_TV);
        tvId = selectedTv.getId();
        tvRepository = TvRepository.getInstance();
        String reviewer = getString(R.string.reviewer);
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
        } else if (v.getId() == R.id.btnFav) {
            Tv selectedTv = getIntent().getParcelableExtra(EXTRA_TV);
            String toastFav = getString(R.string.toastFav);
            String toastFavFail = getString(R.string.toastFavFail);
            long result = tvHelper.insertTv(selectedTv);
            if (result > 0) {
                btnFav.setVisibility(View.GONE);
                btnDel.setVisibility(View.VISIBLE);
                Toast.makeText(DetailTvActivity.this, toastFav, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetailTvActivity.this, toastFavFail, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnDel) {
            Tv selectedTv = getIntent().getParcelableExtra(EXTRA_TV);
            String toastDel = getString(R.string.toastDel);
            tvHelper.deleteTv(selectedTv.getId());
            Toast.makeText(DetailTvActivity.this, toastDel, Toast.LENGTH_SHORT).show();
            btnFav.setVisibility(View.VISIBLE);
            btnDel.setVisibility(View.GONE);
        }
    }
}

