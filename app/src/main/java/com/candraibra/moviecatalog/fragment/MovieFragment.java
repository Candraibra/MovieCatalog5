package com.candraibra.moviecatalog.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.activity.DetailMovieActivity;
import com.candraibra.moviecatalog.activity.SearchMovieActivity;
import com.candraibra.moviecatalog.adapter.MoviePageAdapter;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.network.MoviesRepository;
import com.candraibra.moviecatalog.utils.OnGetPageMovie;
import com.candraibra.moviecatalog.utils.ItemClickSupport;

import java.util.ArrayList;

import static com.candraibra.moviecatalog.activity.SearchMovieActivity.SEARCH_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements View.OnClickListener {

    private final static String LIST_STATE_KEY = "STATE";
    private final GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private MoviesRepository moviesRepository;
    private RecyclerView recyclerView;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private MoviePageAdapter adapter;
    private ProgressBar progressBar;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesRepository = MoviesRepository.getInstance();
        progressBar = view.findViewById(R.id.progressBar);
        TextView btnMore = view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.rv_discover_movie);
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), SearchMovieActivity.class);
                intent.putExtra(SEARCH_MOVIE, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        if (savedInstanceState != null) {
            progressBar.setVisibility(View.INVISIBLE);
            final ArrayList<Movie> moviesState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert moviesState != null;
            movieArrayList.addAll(moviesState);
            adapter = new MoviePageAdapter(getActivity());
            adapter.setMovieList(moviesState);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, moviesState.get(position));
                startActivity(intent);
            });
        } else {
            getMovies(1);
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, movieArrayList);
    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        moviesRepository.getMoviesPage(page, new OnGetPageMovie() {
            @Override
            public void onSuccess(int page, ArrayList<Movie> movies) {
                Log.d("MoviesRepository", "Current Page = " + currentPage);
                progressBar.setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new MoviePageAdapter(getContext());
                    adapter.setMovieList(movies);
                    movieArrayList.addAll(movies);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(manager);
                    ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies.get(position));
                        startActivity(intent);
                    });
                } else {
                    isFetchingMovies = false;
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.toastmsg);
                Toast.makeText(getActivity(), toast_msg, Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_view) {
            Intent intent = new Intent(getActivity(), SearchMovieActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnMore) {
            if (!isFetchingMovies) {
                progressBar.setVisibility(View.VISIBLE);
                getMovies(currentPage + 1);


            }
        }
    }
}
