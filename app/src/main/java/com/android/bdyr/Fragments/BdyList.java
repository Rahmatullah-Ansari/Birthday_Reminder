package com.android.bdyr.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.bdyr.Activities.AddEvent;
import com.android.bdyr.Adapter.EventListAdapter;
import com.android.bdyr.Database.DatabaseManager;
import com.android.bdyr.Database.Entities;
import com.android.bdyr.R;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class BdyList extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ShimmerRecyclerView recyclerView;
    ArrayList<Entities> arrayList;
    EventListAdapter adapter;
    FloatingActionButton button;
    DatabaseManager databaseManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_bdy_list, container, false);
        refreshLayout =view.findViewById(R.id.swipeRefresh1);
        recyclerView=view.findViewById(R.id.list_rv);
        button=view.findViewById(R.id.floating_button);
        arrayList= new ArrayList<>();
        adapter=new EventListAdapter(requireActivity(),arrayList,BdyList.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        databaseManager=DatabaseManager.getINSTANCE(requireActivity());
        loadEvent();
        refreshLayout.setOnRefreshListener(() -> {
            loadEvent();
            refreshLayout.setRefreshing(false);
        });
        button.setOnClickListener(view1 -> {
            Intent intent=new Intent(requireActivity(), AddEvent.class);
            startActivity(intent);
        });
        return view;
    }

    public void loadEvent() {
        recyclerView.showShimmerAdapter();
        arrayList.clear();
        arrayList.addAll(databaseManager.dao().getAllData());
        new Handler().postDelayed(() -> recyclerView.hideShimmerAdapter(),1000 );
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            loadEvent();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}