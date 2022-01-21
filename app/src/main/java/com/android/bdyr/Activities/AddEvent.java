package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.bdyr.databinding.ActivityAddEventBinding;

public class AddEvent extends AppCompatActivity {
    ActivityAddEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Add Event");

    }

}