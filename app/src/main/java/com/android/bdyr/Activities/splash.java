package com.android.bdyr.Activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.bdyr.databinding.ActivitySplashBinding;
import java.util.Objects;
public class splash extends AppCompatActivity {
    ActivitySplashBinding binding;
    private static final long TIMER=1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(1024,1024);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            finish();
        }, TIMER);
    }

}