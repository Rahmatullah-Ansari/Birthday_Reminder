package com.android.bdyr.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bdyr.Activities.AddEvent;
import com.android.bdyr.Counter;
import com.android.bdyr.Database.DatabaseManager;
import com.android.bdyr.Database.Entities;
import com.android.bdyr.Fragments.BdyList;
import com.android.bdyr.Handlers;
import com.android.bdyr.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventListAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Entities> arrayList;
    int count=0,empty=1,not_empty=2;
    String[] months;
    BdyList listener;
    {
        months = new String[]{ "Jan", "Feb", "Mar", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };
    }
    //for timer
    Handler handler=new Handler();
    Runnable runnable;
    String DATE_FORMAT="dd:MM:yyyy HH:mm:ss";
    public EventListAdapter(Context context, ArrayList<Entities> arrayList, BdyList bdyList) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener=bdyList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == empty){
            return new Empty(LayoutInflater.from(context).inflate(R.layout.no_event,parent,false));
        }else if (viewType == not_empty){
            return new Not_empty(LayoutInflater.from(context).inflate(R.layout.birthday_row_item,parent,false));
        }else {
            return null;
        }
    }

    @SuppressLint ("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint ("RecyclerView") int position) {
        if (holder.getClass() == Empty.class){
            Empty container1=(Empty) holder;
            container1.textView.setText("No event are stored now!\nAdd new event by clicking + button\nor pull down to refresh!");
        }else if (holder.getClass() == Not_empty.class){
            Not_empty container=(Not_empty) holder;
            container.flag.setVisibility(View.GONE);
            String cat=arrayList.get(position).getCategory();
            String nam=arrayList.get(position).getName();
            String num=arrayList.get(position).getNumber();
            String dat=arrayList.get(position).getDate();
            String text=arrayList.get(position).getText();
            Counter.counter(arrayList.get(position).getDate(),container.time,arrayList.get(position).getCategory());
            container.name.setText(nam);
            String s = null;
            try {
                s=dat.split(":")[1];
                if (s.startsWith("0")){
                    switch (s) {
                        case "01":
                            s = "1";
                            break;
                        case "02":
                            s = "2";
                            break;
                        case "03":
                            s = "3";
                            break;
                        case "04":
                            s = "4";
                            break;
                        case "05":
                            s = "5";
                            break;
                        case "06":
                            s = "6";
                            break;
                        case "07":
                            s = "7";
                            break;
                        case "08":
                            s = "8";
                            break;
                        case "09":
                            s = "9";
                            break;
                    }
                }
                String month = months[Integer.parseInt(s.trim())-1];
                String[] a=dat.split(":");
                container.date.setText(String.format("%s %s %s , %s",cat,a[0],month,a[2]));
            }catch (Exception e){
                e.printStackTrace();
            }
            container.itemView.setOnClickListener(view -> {
                String cat1 =arrayList.get(position).getCategory();
                String nam1 =arrayList.get(position).getName();
                String num1 =arrayList.get(position).getNumber();
                String dat1 =arrayList.get(position).getDate();
                String text1 =arrayList.get(position).getText();
                String id=arrayList.get(position).getId();
                Intent intent=new Intent(context, AddEvent.class);
                intent.putExtra("cat", cat1);
                intent.putExtra("nam", nam1);
                intent.putExtra("dat", dat1);
                intent.putExtra("num", num1);
                intent.putExtra("text", text1);
                intent.putExtra("id",id);
                intent.putExtra("flag",true);
                context.startActivity(intent);
            });
            container.whatsApp.setOnClickListener(view -> {
                Handlers handlers=new Handlers(context);
                handlers.openWhatsApp(arrayList.get(position).getNumber(),arrayList.get(position).getText());
            });
            container.Call.setOnClickListener(view -> {
                Handlers handlers=new Handlers(context);
                handlers.makeCall(arrayList.get(position).getNumber());
            });
            container.Message.setOnClickListener(view -> {
                Handlers handlers=new Handlers(context);
                handlers.openMessenger(arrayList.get(position).getNumber(),arrayList.get(position).getText());
            });
            container.itemView.setOnLongClickListener(view -> {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("Do you want to delete ?")
                        .setPositiveButton("YES", (dialogInterface, i) -> {
                            DatabaseManager manager=DatabaseManager.getINSTANCE(context);
                            manager.dao().deleteAllData(arrayList.get(position));
                            dialogInterface.dismiss();
                            listener.loadEvent();
                        }).setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                return false;
            });
        }
    }
    @Override
    public int getItemCount() {
        if (arrayList.size() == 0){
            count = -1;
            return count+2;
        }else {
            count=arrayList.size();
            return count;
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (count == -1){
            return empty;
        }else {
            return not_empty;
        }
    }
    public void updateList(ArrayList<Entities> temp) {
        arrayList=temp;
    }

    public static class Empty extends RecyclerView.ViewHolder{
        TextView textView;
        public Empty(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.no_event_text);
        }

    }
    public static class Not_empty extends RecyclerView.ViewHolder{
        TextView name,flag,date;
        public TextView time;
        ImageView whatsApp,Call,Message;
        public Not_empty(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            flag=itemView.findViewById(R.id.flag);
            date=itemView.findViewById(R.id.date);
            whatsApp=itemView.findViewById(R.id.whatsapp);
            Call=itemView.findViewById(R.id.call);
            Message=itemView.findViewById(R.id.message);
            time=itemView.findViewById(R.id.time);
        }

    }
}
