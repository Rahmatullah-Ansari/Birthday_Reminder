package com.android.bdyr.Adapter;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bdyr.Activities.HomeScreen;
import com.android.bdyr.Database.Entities;
import com.android.bdyr.Handlers;
import com.android.bdyr.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class UpcomingAdapter extends RecyclerView.Adapter {
    int count=0;
    Context context;
    ArrayList<Entities> arrayList;
    int empty=0,not_empty=1;
    String[] months;

    {
        months = new String[]{ "Jan", "Feb", "Mar", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };
    }
    public UpcomingAdapter(Context context, ArrayList<Entities> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == empty){
            return new Empty(LayoutInflater.from(context).inflate(R.layout.no_event,parent,false));
        }else if (viewType == not_empty){
            return new Not_empty(LayoutInflater.from(context).inflate(R.layout.birthday_row_item,parent,false));
        }else{
            return null;
        }
    }

    @SuppressLint ({ "SetTextI18n", "DefaultLocale" })
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass() == Empty.class){
            Empty container=(Empty) holder;
        }else if (holder.getClass() == Not_empty.class){
            Not_empty container=(Not_empty) holder;
            container.flag.setVisibility(View.VISIBLE);
            String cat=arrayList.get(position).getCategory();
            String nam=arrayList.get(position).getName();
            String num=arrayList.get(position).getNumber();
            String dat=arrayList.get(position).getDate();
            String text=arrayList.get(position).getText();
            container.name.setText(nam);
            String[] date=dat.split(":");
            String[] current=getCurrentDate().split(":");
            int day=Integer.parseInt(date[0].trim())-Integer.parseInt(current[0]);
            if (current[0].equals(date[0].trim())){
                container.relativeLayout.setBackgroundResource(R.drawable.item_bg_3);
                container.flag.setText("Today");
                popUpNotification(container);
            }
            else if (day == 1){
                container.relativeLayout.setBackgroundResource(R.drawable.item_bg);
                container.flag.setText("Tomorrow");
            }
            else {
                container.relativeLayout.setBackgroundResource(R.drawable.item_bg);
                container.flag.setText(String.format("%d days to go",day-1));
            }
            String s=date[1];
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
        }
    }

    private void popUpNotification(Not_empty holder) {
        String name=arrayList.get(holder.getAdapterPosition()).getName();
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context, String.valueOf(123));
        Intent intent=new Intent(context, HomeScreen.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.bdy);
        builder.setContentTitle(name + " birthday today,wish them!");
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        Notification notification=builder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("123","my notification", NotificationManager.IMPORTANCE_DEFAULT);
            //channel.setDescription("My notification");
            NotificationManager manager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.notify(123,notification);
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
    @Override
    public int getItemViewType(int position) {
        if(count == -1){
            return empty;
        }else {
            return not_empty;
        }
    }
    public static class Empty extends RecyclerView.ViewHolder{

        public Empty(@NonNull View itemView) {
            super(itemView);
        }

    }
    public static class Not_empty extends RecyclerView.ViewHolder{
        TextView name,flag,date;
        ImageView whatsApp,Call,Message;
        RelativeLayout relativeLayout;
        public Not_empty(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            flag=itemView.findViewById(R.id.flag);
            date=itemView.findViewById(R.id.date);
            whatsApp=itemView.findViewById(R.id.whatsapp);
            Call=itemView.findViewById(R.id.call);
            Message=itemView.findViewById(R.id.message);
            relativeLayout=itemView.findViewById(R.id.relativeLayout);
        }

    }
}
