package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.bdyr.Database.DatabaseManager;
import com.android.bdyr.Database.Entities;
import com.android.bdyr.databinding.ActivityAddEventBinding;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {
    ActivityAddEventBinding binding;
    DatePickerDialog.OnDateSetListener listener;
    String category,name,date,number,text;
    @SuppressLint ("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getBooleanExtra("flag",false)){
            setTitle("Update");
            name=getIntent().getStringExtra("nam");
            number=getIntent().getStringExtra("num");
            date=getIntent().getStringExtra("dat");
            category=getIntent().getStringExtra("cat");
            text=getIntent().getStringExtra("text");
            if (category.equals("Birthday")){
                binding.birthday.setChecked(true);
                binding.anniversary.setChecked(false);
            }else {
                binding.birthday.setChecked(false);
                binding.anniversary.setChecked(true);
            }
            binding.nameLabel.setText(name);
            binding.numberLabel.setText(number);
            binding.dateLabel.setText(date);
            binding.wishLabel.setText(text);
        }else {
            setTitle("Add Event");
        }
        DatabaseManager databaseManager=DatabaseManager.getINSTANCE(this);
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
        binding.saveButton.setOnClickListener(view -> {
            if (binding.birthday.isChecked()){
                category="Birthday";
            }else if (binding.anniversary.isChecked()){
                category="Anniversary";
            }else {
                category="";
            }
            name=binding.nameLabel.getText().toString();
            date=binding.dateLabel.getText().toString();
            text=binding.wishLabel.getText().toString();
            number=binding.numberLabel.getText().toString();
            if (category.equals("")){
                Toast.makeText(AddEvent.this, "select category please", Toast.LENGTH_SHORT).show();
            }else if (name.equals("")){
                Toast.makeText(AddEvent.this, "enter name please", Toast.LENGTH_SHORT).show();
            }else if (number.length() < 10){
                Toast.makeText(AddEvent.this, "enter valid number", Toast.LENGTH_SHORT).show();
            }else if (text.equals("")){
                Toast.makeText(AddEvent.this, "enter wish text", Toast.LENGTH_SHORT).show();
            }else {
                Entities data;
                int Id=databaseManager.dao().getId(0);
                if (Id == 0){
                    data=new Entities(0,name,date,number,category,text);
                }else {
                    data=new Entities(Id+1,name,date,number,category,text);
                }
                databaseManager.dao().insertData(data);
                Toast.makeText(AddEvent.this, "Event added successfully!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this::finish, 800);
            }
        });
    }

}