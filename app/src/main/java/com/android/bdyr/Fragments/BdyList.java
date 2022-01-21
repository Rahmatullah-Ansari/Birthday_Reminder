package com.android.bdyr.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bdyr.Adapter.EventListAdapter;
import com.android.bdyr.Holder.EventHolder;
import com.android.bdyr.R;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;

public class BdyList extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ShimmerRecyclerView recyclerView;
    ArrayList<EventHolder> arrayList;
    EventListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_bdy_list, container, false);
        refreshLayout =view.findViewById(R.id.swipeRefresh1);
        recyclerView=view.findViewById(R.id.list_rv);
        arrayList=new ArrayList<>();
        adapter=new EventListAdapter(requireActivity(),arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        loadEvent();
        refreshLayout.setOnRefreshListener(() -> {
            loadEvent();
            refreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void loadEvent() {
        recyclerView.showShimmerAdapter();


        new Handler().postDelayed(() -> recyclerView.hideShimmerAdapter(),1000 );
    }

}