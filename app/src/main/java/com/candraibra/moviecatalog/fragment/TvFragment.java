package com.candraibra.moviecatalog.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.activity.DetailTvActivity;
import com.candraibra.moviecatalog.activity.SearchTvActivity;
import com.candraibra.moviecatalog.adapter.TvPageAdapter;
import com.candraibra.moviecatalog.model.Tv;
import com.candraibra.moviecatalog.network.OnGetPageTv;
import com.candraibra.moviecatalog.network.TvRepository;
import com.candraibra.moviecatalog.utils.ItemClickSupport;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFragment extends Fragment implements View.OnClickListener {

    private final static String LIST_STATE_KEY2 = "STATE2";
    private final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
    private ArrayList<Tv> tvArrayList = new ArrayList<>();
    private TvRepository tvRepository;
    private RecyclerView recyclerView;
    private boolean isFetchingTv;
    private int currentPage = 1;
    private TvPageAdapter adapter;
    private ProgressBar progressBar;

    public TvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvRepository = TvRepository.getInstance();
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.rv_discover_tv);
        ConstraintLayout searchview = view.findViewById(R.id.search_view);
        if (savedInstanceState != null) {
            progressBar.setVisibility(View.INVISIBLE);
            final ArrayList<Tv> tvState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY2);
            assert tvState != null;
            tvArrayList.addAll(tvState);
            adapter = new TvPageAdapter(getContext());
            adapter.setTvList(tvState);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailTvActivity.class);
                intent.putExtra(DetailTvActivity.EXTRA_TV, tvState.get(position));
                startActivity(intent);
            });
        } else {
            getTv(currentPage);
            setupOnScrollListener();
        }
    }

    private void setupOnScrollListener() {

        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingTv) {
                        progressBar.setVisibility(View.INVISIBLE);
                        getTv(currentPage + 1);
                    }
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY2, tvArrayList);
    }

    private void getTv(int page) {
        isFetchingTv = true;
        tvRepository.getTvPage(page, new OnGetPageTv() {
            @Override
            public void onSuccess(int page, ArrayList<Tv> tvs) {
                if (adapter == null) {
                    adapter = new TvPageAdapter(getContext());
                    adapter.setTvList(tvs);
                    tvArrayList.addAll(tvs);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(getActivity(), DetailTvActivity.class);
                        intent.putExtra(DetailTvActivity.EXTRA_TV, tvs.get(position));
                        startActivity(intent);
                    });
                } else {
                    adapter.appendTv(tvs);
                    adapter.notifyDataSetChanged();

                }
                currentPage = page;
                isFetchingTv = false;
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
            Intent intent = new Intent(getActivity(), SearchTvActivity.class);
            startActivity(intent);
        }
    }
}
