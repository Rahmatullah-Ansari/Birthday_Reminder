package com.android.bdyr.Fragments;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.bdyr.Activities.AddEvent;
import com.android.bdyr.Activities.Setting;
import com.android.bdyr.Adapter.EventListAdapter;
import com.android.bdyr.Database.DatabaseManager;
import com.android.bdyr.Database.Entities;
import com.android.bdyr.R;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

public class BdyList extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ShimmerRecyclerView recyclerView;
    ArrayList<Entities> arrayList;
    EventListAdapter adapter;
    FloatingActionButton button;
    DatabaseManager databaseManager;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_bdy_list, container, false);
        reference= FirebaseDatabase.getInstance().getReference("Bdyr");
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
        ShortList();
    }

    private void ShortList() {
        for (int i=0;i<arrayList.size();i++){
            for (int j=i+1;j<arrayList.size();j++){
                String date=arrayList.get(i).getDate();
                String date1=arrayList.get(j).getDate();
                String month=date.split(":")[1].trim();
                String month1=date1.split(":")[1].trim();
                if ((Integer.parseInt(month)) > (Integer.parseInt(month1))){
                    Entities entities=arrayList.get(i);
                    arrayList.set(i,arrayList.get(j));
                    arrayList.set(j,entities);
                }
            }
        }
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

    public void backup() {
        ProgressDialog dialog=new ProgressDialog(requireActivity());
        dialog.setTitle("Backup");
        dialog.setMessage("Backing Up....");
        dialog.setCancelable(false);
        if (arrayList.size() > 0){
            dialog.show();
            for (Entities entities:arrayList){
                String number=entities.getNumber();
                reference.child(getIpAddress()).child(number).setValue(entities);
            }
            dialog.dismiss();
            Toast.makeText(requireActivity(), "Backup successful", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(requireActivity(), "Nothing is to backup!", Toast.LENGTH_SHORT).show();
        }
    }

    public void restore() {
        ProgressDialog dialog=new ProgressDialog(requireActivity());
        dialog.setTitle("Restore");
        dialog.setMessage("Restoring....");
        dialog.setCancelable(false);
        reference.child(getIpAddress()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    dialog.show();
                    arrayList.clear();
                    DatabaseManager manager=DatabaseManager.getINSTANCE(requireActivity());
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Entities entities=dataSnapshot.getValue(Entities.class);
                        manager.dao().insertData(entities);
                    }
                    dialog.dismiss();
                    Toast.makeText(requireActivity(), "Restore successful", Toast.LENGTH_SHORT).show();
                    if (getShared(Setting.FOLDER)){
                        reference.child(getIpAddress()).removeValue();
                    }
                    loadEvent();
                }else{
                    Toast.makeText(requireActivity(), "Sorry no backup found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean getShared(String deleteBackup) {
        SharedPreferences sharedPreferences=requireActivity().getSharedPreferences(deleteBackup,requireActivity().MODE_PRIVATE);
        return sharedPreferences.getBoolean(deleteBackup,false);
    }
    private String getIpAddress(){
        String ip="";
        try {
            for (Enumeration<NetworkInterface> enumeration=NetworkInterface.getNetworkInterfaces();enumeration.hasMoreElements();){
                 NetworkInterface networkInterface=enumeration.nextElement();
                 for (Enumeration<InetAddress> enumeration1=networkInterface.getInetAddresses();enumeration1.hasMoreElements();){
                     InetAddress inetAddress=enumeration1.nextElement();
                     if (!inetAddress.isLoopbackAddress()){
                         ip=Formatter.formatIpAddress(inetAddress.hashCode());
                     }
                 }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return ip.replace(".","");
    }
}