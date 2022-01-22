package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.android.bdyr.databinding.ActivityAddEventBinding;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {
    ActivityAddEventBinding binding;
    DatePickerDialog.OnDateSetListener listener;
    @SuppressLint ("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Add Event");
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        if (month < 10){
            binding.dateLabel.setText(String.format("%d : 0%d : %d",day,month+1,year));
        }else {
            binding.dateLabel.setText(String.format("%d : %d : %d",day,month+1,year));
        }
        binding.dateLabel.setOnClickListener(view -> {
            DatePickerDialog dialog=new DatePickerDialog(AddEvent.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,listener,year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        listener= (datePicker, year1, month1, day1) -> {
            month1=month1+1;
            binding.dateLabel.setText(String.format("%d : %d : %d",day1,month1,year1));
        };
        binding.wishLabel.setVerticalScrollBarEnabled(true);
    }

}