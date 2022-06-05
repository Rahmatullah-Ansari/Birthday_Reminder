package com.android.bdyr;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.android.bdyr.Activities.HomeScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Counter {
    private  static Handler handler=new Handler();
    private  static Runnable runnable;
    public static void counter(String date, TextView container, String category) {
        runnable = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    String d = date.replace(" ", "");
                    String[] array = d.split(":");
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    Date futureDate = dateFormat.parse(array[0] + "-" + array[1] + "-" + year + " 12:00 AM");
                    Date currentDate = new Date();
                    container.setVisibility(View.VISIBLE);
                    if (!currentDate.equals(futureDate)) {
                        long diff = futureDate.getTime() - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        if (days > 0){
                            container.setText(String.format("%02d days %02d hours %02d minutes %02d seconds left", days, hours, minutes, seconds));
                        }else {
                            container.setText(category+" passed,wait for next");
                        }
                    }else if(currentDate.equals(futureDate)){
                        container.setText(category+" Today,Wish Them!");

                    }else{
                        container.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
        HomeScreen screen=new HomeScreen();
    }
    public static void destroyed(){
        if(handler != null && runnable != null){
            handler.removeCallbacks(runnable);
        }
    }
}
