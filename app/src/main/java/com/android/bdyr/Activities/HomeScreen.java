package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.bdyr.databinding.ActivityHomeScreenBinding;

import java.util.Objects;

public class HomeScreen extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(1024,1024);
    }

}