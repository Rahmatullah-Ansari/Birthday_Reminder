package com.android.bdyr.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.bdyr.Adapter.viewPagerAdapter;
import com.android.bdyr.Counter;
import com.android.bdyr.Fragments.BdyList;
import com.android.bdyr.Fragments.UpcomingBdy;
import com.android.bdyr.R;
import com.android.bdyr.databinding.ActivityHomeScreenBinding;
public class HomeScreen extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    com.android.bdyr.Adapter.viewPagerAdapter viewPagerAdapter;
    @SuppressLint ("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toLowerCase());
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        int count=binding.viewPager.getCurrentItem();
        if (count == 0){
            UpcomingBdy fragment=(UpcomingBdy) binding.viewPager.getAdapter().instantiateItem(binding.viewPager,count);
            fragment.search(text);
        }else if (count == 1){
            BdyList fragment=(BdyList) binding.viewPager.getAdapter().instantiateItem(binding.viewPager,count);
            fragment.search(text);
        }
    }

    @SuppressLint ("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.export:
                BackupEvent();
                break;
            case R.id.importEvent:
                RestoreEvent();
                break;
            case R.id.setting:
                startActivity(new Intent(HomeScreen.this,Setting.class));
                break;
            case R.id.about:
                startActivity(new Intent(HomeScreen.this,About.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void BackupEvent() {
        if (check_network_state()){
            BdyList fragment=(BdyList) binding.viewPager.getAdapter().instantiateItem(binding.viewPager,1);
            fragment.backup();
        }else{
            Toast.makeText(HomeScreen.this, "Check internet connection and try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void RestoreEvent() {
        if (check_network_state()){
            BdyList fragment=(BdyList) binding.viewPager.getAdapter().instantiateItem(binding.viewPager,1);
            fragment.restore();
        }else{
            Toast.makeText(HomeScreen.this, "Check internet connection and try again!", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean check_network_state(){
        ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint ("MissingPermission") NetworkInfo info=manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    @Override
    protected void onStop() {
        Counter.destroyed();
        super.onStop();
    }
}