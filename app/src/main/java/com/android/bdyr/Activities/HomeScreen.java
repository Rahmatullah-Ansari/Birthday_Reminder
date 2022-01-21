package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.android.bdyr.Adapter.viewPagerAdapter;
import com.android.bdyr.R;
import com.android.bdyr.databinding.ActivityHomeScreenBinding;

public class HomeScreen extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    com.android.bdyr.Adapter.viewPagerAdapter viewPagerAdapter;
    @SuppressLint ("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Objects.requireNonNull(getSupportActionBar()).hide();
        //getWindow().setFlags(1024,1024);
        viewPagerAdapter=new viewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.setHorizontalScrollBarEnabled(true);
        binding.bottomNavigation.setItemHorizontalTranslationEnabled(true);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.upcoming_item:
                    binding.viewPager.setCurrentItem(0);
                    break;
                case R.id.list_item:
                    binding.viewPager.setCurrentItem(1);
                    break;
            }
            return true;
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        binding.bottomNavigation.getMenu().findItem(R.id.upcoming_item).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.list_item).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}