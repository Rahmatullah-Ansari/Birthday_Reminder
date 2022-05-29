package com.android.bdyr;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.widget.TextView;

import com.android.bdyr.Adapter.EventListAdapter;
import com.android.bdyr.Adapter.UpcomingAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Counter {
    private  static Handler handler;

    public static void countDownStart(String date, TextView container) {
        handler = new Handler();
        Runnable runnable = new Runnable() {
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
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime() - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        container.setText(String.format("%02d days %02d hours %02d minutes %02d seconds left", days, hours, minutes, seconds));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
