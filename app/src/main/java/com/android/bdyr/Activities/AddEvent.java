package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.bdyr.Database.DatabaseManager;
import com.android.bdyr.Database.Entities;
import com.android.bdyr.databinding.ActivityAddEventBinding;

import java.util.Calendar;
import java.util.UUID;

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
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        boolean value=getIntent().getBooleanExtra("flag",false);
        String id=getIntent().getStringExtra("id");
        if (value){
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
            if (month < 10){
                if (day < 10){
                    binding.dateLabel.setText(String.format("0%d : 0%d : %d",day,month+1,year));
                }else {
                    binding.dateLabel.setText(String.format("%d : 0%d : %d",day,month+1,year));
                }
            }else {
                binding.dateLabel.setText(String.format("%d : %d : %d",day,month+1,year));
            }
        }
        DatabaseManager databaseManager=DatabaseManager.getINSTANCE(this);
        binding.dateLabel.setOnClickListener(view -> {
            DatePickerDialog dialog=new DatePickerDialog(AddEvent.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,listener,year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        listener= (datePicker, year1, month1, day1) -> {
            month1=month1+1;
            if (month1 < 10){
                if (day1 < 10){
                    binding.dateLabel.setText(String.format("0%d : 0%d : %d",day1,month1,year1));
                }else {
                    binding.dateLabel.setText(String.format("%d : 0%d : %d",day1,month1,year1));
                }
            }else {
                binding.dateLabel.setText(String.format("%d : %d : %d",day1,month1,year1));
            }
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
            }
            else if (value){
                DatabaseManager manager=DatabaseManager.getINSTANCE(AddEvent.this);
                manager.dao().update(name,date,number,category,text,id);
                Toast.makeText(AddEvent.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this::finish, 800);
            }
            else {
                Entities data;
                String Id= UUID.randomUUID().toString();
                data=new Entities(Id,name,date,number,category,text);
                databaseManager.dao().insertData(data);
                Toast.makeText(AddEvent.this, "Event added successfully!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this::finish, 800);
            }
        });
    }

}