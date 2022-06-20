package com.android.bdyr.Fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.bdyr.Adapter.UpcomingAdapter;
import com.android.bdyr.Database.DatabaseManager;
import com.android.bdyr.Database.Entities;
import com.android.bdyr.R;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class UpcomingBdy extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ShimmerRecyclerView recyclerView;
    ArrayList<Entities> arrayList;
    UpcomingAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_upcoming_bdy, container, false);
        refreshLayout=view.findViewById(R.id.swipeRefresh);
        recyclerView=view.findViewById(R.id.upcoming_rv);
        arrayList=new ArrayList<>();
        adapter=new UpcomingAdapter(requireActivity(),arrayList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        loadUpcomingEvent();
        refreshLayout.setOnRefreshListener(() -> {
            loadUpcomingEvent();
            refreshLayout.setRefreshing(false);
        });
        return view;
    }

    @SuppressLint ("NotifyDataSetChanged")
    private void loadUpcomingEvent() {
        try {
            recyclerView.showShimmerAdapter();
            arrayList.clear();
            DatabaseManager databaseManager=DatabaseManager.getINSTANCE(requireActivity());
            ArrayList<Entities> temp = new ArrayList<>(databaseManager.dao().getAllData());
            String[] current_date=getCurrentDate().split(":");
            for (Entities entities:temp){
                String date=entities.getDate();
                String[] array =date.split(":");
                if (current_date[1].equals(array[1].trim()) && (Integer.parseInt(current_date[0]) <= Integer.parseInt(array[0].trim()))){
                    arrayList.add(entities);
                }
            }
            adapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> recyclerView.hideShimmerAdapter(),1000 );
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String getCurrentDate() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Log.d(TAG, "getCurrentDateTime: greater than O");
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd:MM:yyyy"));
        } else {
            //Log.d(TAG, "getCurrentDateTime: less than O");
            @SuppressLint ("SimpleDateFormat") SimpleDateFormat SDFormat = new SimpleDateFormat("dd:MM:yyyy");
            return SDFormat.format(new Date());
        }
    }

    @SuppressLint ("NotifyDataSetChanged")
    public void search(String text) {
        ArrayList<Entities> temp=new ArrayList<>();
        if (text.isEmpty()){
            temp.addAll(arrayList);
        }else {
            for (Entities entities:arrayList){
                if (entities.getName().toLowerCase().contains(text)){
                    temp.add(entities);
                }
            }
        }
        adapter.updateList(temp);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            loadUpcomingEvent();
        }
    }
}