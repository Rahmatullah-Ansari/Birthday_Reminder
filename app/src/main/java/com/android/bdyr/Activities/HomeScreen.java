package com.android.bdyr.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.bdyr.Adapter.viewPagerAdapter;
import com.android.bdyr.BuildConfig;
import com.android.bdyr.Fragments.BdyList;
import com.android.bdyr.Fragments.UpcomingBdy;
import com.android.bdyr.R;
import com.android.bdyr.databinding.ActivityHomeScreenBinding;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

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
                new AlertDialog.Builder(this).setCancelable(false)
                        .setTitle("Export")
                        .setMessage("where do you want to export ?")
                        .setPositiveButton("Others", (dialogInterface, i) -> {
                            String path=getDatabasePath("EVENTS").getAbsolutePath();
                            File file=new File(path);
                            Intent intent=new Intent(Intent.ACTION_SEND);
                            intent.setType("*/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+"."+getLocalClassName()+".provider",file));
                            startActivity(intent);
                        })
                        .setNegativeButton("On device", (dialogInterface, i) ->{
                            checkPermissionOfReadWrite();
                            dialogInterface.dismiss();
                        }).setNeutralButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                break;
            case R.id.importEvent:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkPermissionOfReadWrite() {
        if (ContextCompat.checkSelfPermission(HomeScreen.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(HomeScreen.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Requestpermission();
        }else {
            saveOnDevice();
        }
    }

    private void Requestpermission() {
        ActivityCompat.requestPermissions(HomeScreen.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE },26);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 26){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                saveOnDevice();
            }
            else {
                Toast.makeText(HomeScreen.this, "Permission required!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveOnDevice() {
        try {
            String path=getDatabasePath("EVENTS").getAbsolutePath();
            File db=new File(path);
            File f= new File(Environment.getExternalStorageDirectory() + File.separator + "Birthday Reminder");
            if (!f.exists() && !f.isDirectory()){
                if (f.mkdir()){
                    Toast.makeText(HomeScreen.this, "Directory created!", Toast.LENGTH_SHORT).show();
                }
            }
            String folder = Environment.getExternalStorageDirectory() + File.separator + "Birthday Reminder"+File.separator+"EVENTS.db" ;
            File file=new File(folder);
            file.setWritable(true);
            if (file.exists() && file.isFile()){
                file.delete();
            }
            file.createNewFile();
            if (copyFile(new FileInputStream(db),new FileOutputStream(file))){
                Toast.makeText(HomeScreen.this, "saved on location == "+folder, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean copyFile(FileInputStream fis, FileOutputStream fos) {
        FileChannel from=null;
        FileChannel to=null;
        boolean val=false;
        try {
            from=fis.getChannel();
            to=fos.getChannel();
            from.transferTo(0,from.size(),to);
            val=true;
        }catch (Exception e){
            e.printStackTrace();
            val=false;
        }
        return val;
    }

}