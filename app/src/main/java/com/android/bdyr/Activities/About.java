package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.android.bdyr.databinding.ActivityAboutBinding;

public class About extends AppCompatActivity {
    ActivityAboutBinding binding;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        textView=binding.version;
        try {
            PackageInfo info=getPackageManager().getPackageInfo(getPackageName(),0);
            int versionCode=info.versionCode;
            textView.setText("Version : "+versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}