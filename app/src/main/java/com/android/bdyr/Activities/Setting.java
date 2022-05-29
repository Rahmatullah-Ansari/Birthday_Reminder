package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.android.bdyr.databinding.ActivitySettingBinding;

public class Setting extends AppCompatActivity {
    ActivitySettingBinding binding;
    SwitchCompat switchCompat;
    public static final String FOLDER="deleteBackup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        switchCompat=binding.deleteSwitch;
        boolean value=getShared(FOLDER);
        switchCompat.setChecked(value);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchCompat.setChecked(switchCompat.isChecked());
                setShared(FOLDER,switchCompat.isChecked());
            }
        });
    }

    public boolean getShared(String deleteBackup) {
        SharedPreferences sharedPreferences=getSharedPreferences(deleteBackup,MODE_PRIVATE);
        return sharedPreferences.getBoolean(deleteBackup,false);
    }

    public void setShared(String deleteBackup, boolean checked) {
        SharedPreferences sharedPreferences=getSharedPreferences(deleteBackup,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(deleteBackup,checked);
        editor.apply();
    }
}