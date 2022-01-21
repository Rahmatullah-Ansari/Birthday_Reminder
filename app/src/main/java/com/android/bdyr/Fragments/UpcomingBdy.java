package com.android.bdyr.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bdyr.Adapter.UpcomingAdapter;
import com.android.bdyr.Holder.EventHolder;
import com.android.bdyr.R;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;

public class UpcomingBdy extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ShimmerRecyclerView recyclerView;
    ArrayList<EventHolder> arrayList;
    UpcomingAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_upcoming_bdy, container, false);
        refreshLayout=view.findViewById(R.id.swipeRefresh);
        recyclerView=view.findViewById(R.id.upcoming_rv);
        arrayList=new ArrayList<>();
        adapter=new UpcomingAdapter(requireActivity(),arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        loadUpcomingEvent();
        refreshLayout.setOnRefreshListener(() -> {
            loadUpcomingEvent();
            refreshLayout.setRefreshing(false);
        });
        return view;
    }

    private void loadUpcomingEvent() {
        recyclerView.showShimmerAdapter();



        new Handler().postDelayed(() -> recyclerView.hideShimmerAdapter(),1000 );
    }

}