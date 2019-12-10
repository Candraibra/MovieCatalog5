package com.candraibra.moviecatalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.adapter.TvPageAdapter;
import com.candraibra.moviecatalog.model.Tv;
import com.candraibra.moviecatalog.utils.OnGetSearchTv;
import com.candraibra.moviecatalog.network.TvRepository;
import com.candraibra.moviecatalog.utils.ItemClickSupport;

import java.util.ArrayList;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class SearchTvActivity extends AppCompatActivity implements View.OnClickListener {
    public static String SEARCH_TV = "query";
    RecyclerView rvSearch;
    TvPageAdapter adapter;
    TvRepository tvRepository;
    ProgressBar progressBar;
    TextView title, btnBack;
    String query;
    private ArrayList<Tv> tvArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv);
        rvSearch = findViewById(R.id.rv_search_tv);
        progressBar = findViewById(R.id.progressBar);
        tvRepository = TvRepository.getInstance();
        btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(this);
        title = findViewById(R.id.tv_title);

        Intent intent = getIntent();
        query = intent.getStringExtra(SEARCH_TV);

        showRecycler();
        getSearchTv(query);
    }

    private void showRecycler() {
        adapter = new TvPageAdapter(this);
        rvSearch.setLayoutManager(new GridLayoutManager(this, 3));
        rvSearch.setHasFixedSize(true);

    }

    private void getSearchTv(String query) {
        tvRepository.getSearchTv(query, new OnGetSearchTv() {
            @Override
            public void onSuccess(ArrayList<Tv> tvs) {
                progressBar.setVisibility(View.GONE);
                adapter.setTvList(tvs);
                rvSearch.setAdapter(adapter);
                title.setText(query);
                tvArrayList.addAll(tvs);
                ItemClickSupport.addTo(rvSearch).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(SearchTvActivity.this, DetailTvActivity.class);
                    intent.putExtra(DetailTvActivity.EXTRA_TV, tvs.get(position));
                    startActivity(intent);
                });
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.toastmsg);
                Toast.makeText(SearchTvActivity.this, toast_msg, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
            onBackPressed();
            {
                finish();
            }
        }
    }
}
