package com.candraibra.moviecatalog.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.activity.DetailMovieActivity;
import com.candraibra.moviecatalog.activity.DetailTvActivity;
import com.candraibra.moviecatalog.activity.SearchActivity;
import com.candraibra.moviecatalog.activity.SettingActivity;
import com.candraibra.moviecatalog.adapter.MovieAdapter;
import com.candraibra.moviecatalog.adapter.TvAdapter;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.model.Tv;
import com.candraibra.moviecatalog.network.MoviesRepository;
import com.candraibra.moviecatalog.network.OnGetMoviesCallback;
import com.candraibra.moviecatalog.network.OnGetTvCallback;
import com.candraibra.moviecatalog.network.TvRepository;
import com.candraibra.moviecatalog.utils.ItemClickSupport;

import java.util.ArrayList;

public class PopularFragment extends Fragment implements View.OnClickListener {

    private final static String LIST_STATE_KEY = "STATE";
    private final static String LIST_STATE_KEY2 = "STATE2";
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private ArrayList<Tv> tvArrayList = new ArrayList<>();
    private MovieAdapter adapter;
    private TvAdapter adapter2;
    private RecyclerView rvPopular, rvPopular2;
    private MoviesRepository moviesRepository;
    private TvRepository tvRepository;
    private ProgressBar progressBar;


    public PopularFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btnSetting = view.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);
        View searchView = view.findViewById(R.id.search_view);
        searchView.setOnClickListener(this);
        rvPopular = view.findViewById(R.id.rv_popular_movie);
        rvPopular2 = view.findViewById(R.id.rv_popular_tv);
        progressBar = view.findViewById(R.id.progressBar);
        rvPopular.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvPopular.setHasFixedSize(true);
        moviesRepository = MoviesRepository.getInstance();
        rvPopular2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvPopular2.setHasFixedSize(true);
        tvRepository = TvRepository.getInstance();
        if (savedInstanceState != null) {
            progressBar.setVisibility(View.INVISIBLE);
            final ArrayList<Movie> moviesState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert moviesState != null;
            movieArrayList.addAll(moviesState);
            adapter = new MovieAdapter(getActivity());
            adapter.setMovieList(moviesState);
            rvPopular.setAdapter(adapter);
            final ArrayList<Tv> tvState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY2);
            assert tvState != null;
            tvArrayList.addAll(tvState);
            adapter2 = new TvAdapter(getContext());
            adapter2.setTvList(tvState);
            rvPopular2.setAdapter(adapter2);
            ItemClickSupport.addTo(rvPopular).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, moviesState.get(position));
                startActivity(intent);
            });
            ItemClickSupport.addTo(rvPopular2).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailTvActivity.class);
                intent.putExtra(DetailTvActivity.EXTRA_TV, tvState.get(position));
                startActivity(intent);
            });
        } else {
            getData();

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, movieArrayList);
        outState.putParcelableArrayList(LIST_STATE_KEY2, tvArrayList);
    }

    private void getData() {
        moviesRepository.getMoviesPopular(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(final ArrayList<Movie> movies) {
                adapter = new MovieAdapter(getContext());
                adapter.setMovieList(movies);
                rvPopular.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                movieArrayList.addAll(movies);
                ItemClickSupport.addTo(rvPopular).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                    intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies.get(position));
                    startActivity(intent);
                });
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.toastmsg);
                Toast.makeText(getActivity(), toast_msg, Toast.LENGTH_SHORT).show();
            }
        });
        tvRepository.getTvPopular(new OnGetTvCallback() {
            @Override
            public void onSuccess(final ArrayList<Tv> tvs) {
                adapter2 = new TvAdapter(getActivity());
                adapter2.setTvList(tvs);
                rvPopular2.setAdapter(adapter2);
                progressBar.setVisibility(View.GONE);
                tvArrayList.addAll(tvs);
                ItemClickSupport.addTo(rvPopular2).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(getActivity(), DetailTvActivity.class);
                    intent.putExtra(DetailTvActivity.EXTRA_TV, tvs.get(position));
                    startActivity(intent);
                });
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.toastmsg);
                Toast.makeText(getActivity(), toast_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSetting) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.search_view) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        }
    }
}
