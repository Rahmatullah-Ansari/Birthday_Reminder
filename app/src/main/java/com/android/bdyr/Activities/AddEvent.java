package com.android.bdyr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.bdyr.Adapter.wishAdapter;
import com.android.bdyr.Database.DatabaseManager;
import com.android.bdyr.Database.Entities;
import com.android.bdyr.Helper;
import com.android.bdyr.R;
import com.android.bdyr.databinding.ActivityAddEventBinding;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddEvent extends AppCompatActivity implements Helper {
    ActivityAddEventBinding binding;
    DatePickerDialog.OnDateSetListener listener;
    String category,name,date,number,text;
    int val=0;
    View view1;
    ArrayList<String> arrayList;
    ShimmerRecyclerView recyclerView;
    wishAdapter adapter;
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
        arrayList=new ArrayList<>();
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
        binding.wishLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (val==0){
                    view1=LayoutInflater.from(AddEvent.this).inflate(R.layout.wish_layout,null);
                    recyclerView=view1.findViewById(R.id.wish_rv);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AddEvent.this));
                    adapter=new wishAdapter(AddEvent.this,arrayList,AddEvent.this);
                    recyclerView.setAdapter(adapter);
                    loadArrayList();
                    AlertDialog.Builder builder=new AlertDialog.Builder(AddEvent.this);
                    builder.setTitle("Select wish");
                    builder.setCancelable(false);
                    builder.setView(view1);
                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("custom", (dialogInterface, i) -> {
                        val=1;
                        dialogInterface.dismiss();
                    });
                    builder.show();
                }
            }
        });
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

    private void loadArrayList() {
        arrayList.add("Live your life with smiles, not tears. Beat your age with friends and not years. Happy birthday!");
        arrayList.add("Happy birthday. I pray all your birthday wishes to come true.");
        arrayList.add("Here is a wish for your birthday. May you receive whatever you ask for, may you find whatever you seek. Happy birthday.");
        arrayList.add("May all the joy you have spread around come back to you a hundredfold. Happy birthday.");
        arrayList.add("This birthday I wish you and your family abundance, happiness, and health. May lady luck come especially for the birthday boy/girl.");
        arrayList.add("May you receive the greatest of joys and everlasting bliss. You are a gift yourself, and you deserve the best of everything. Happy birthday.");
        arrayList.add("Forget the past; it is gone. Do not think of the future; it has not come. But live in the present because it's a gift and that's why it's called the present. Happy birthday.");
        arrayList.add("You are the sweetest person I know, and this birthday is a fresh beginning. I wish you confidence, courage, and capability. Happy birthday.");
        arrayList.add("Your birthday has come around after 365 days. That is a pretty long time. Deal with the pressure because that how diamonds are made. Happy birthday.");
        arrayList.add(" It is my superiority complex that has made me wish you for your birthday so early. Now I can strut about and tell all your other well-wishers that I was the first!");
        arrayList.add("I am sending you good vibes and smiles for every second of your special day. Have a fantastic birthday, gorgeous.");
        arrayList.add("I hope that all your wishes come true. Don’t tell anyone because I’ll deny it, but you are my favorite sister! Happy birthday.");
        arrayList.add("My life was black and white before I met you, but you have filled my world with colour and beauty. Happy birthday, my love. We’ve been watching too much Wanda Vision by the way.");
        arrayList.add("The world is a better place since you've been in it. Happy birthday handsome!");
        arrayList.add("No matter how serious life gets, you've got to have that one person you can be completely stupid with. So glad I've got you bro! Have a brilliant birthday!");
        arrayList.add("I wanted to get you something truly amazing and inspiring for your birthday and then I remembered that you already have me. You're welcome. Happy birthday!");
        arrayList.add("It’s easy to fall in love, but staying in love with the same person for the rest of one’s life is considerably more difficult. May God provide us the strength to stay committed to one another. Happy Anniversary!");
        arrayList.add("The sound of the sea and the echo of your love share a few characteristics in common: they are both constant and eternal. Happy Anniversary!");
        arrayList.add("Everything was like a dark sky until you, my brightest star, came through. We’ve had our ups and downs, but my heart always knew we’d make it this far. Happy Anniversary!");
        arrayList.add("Our anniversary will remind you of the happiest times of our relationship, but it will also remind us of the hardships you had to through to get to those happy times. I wish you the best!");
        arrayList.add("I hope this adventure could go on forever because of the way you love me and the way you touch my life. Thank you for making this a memorable day in my life. Happy Anniversary!");
        arrayList.add("May the light of happiness constantly shine through the clouds of misunderstandings in our eternal relationship, forming a rainbow of love. Happy Anniversary.");
        arrayList.add("It takes time to build the kind of relationship that’s almost entirely perfect. We have seemed to have done it all in one year. Happy Anniversary!");
        arrayList.add("I wish you a happy anniversary, my love. Remember the good times and leave the bad behind; reminisce about joyful memories while ignoring the sad. Take pleasure in a love that has endured so long that even angels are singing a song of joy.");
        arrayList.add("May this day bring you a lot of happiness. May the years ahead of us be spent loving and caring for one another. Happy Anniversary!");
        arrayList.add("Anniversaries are days to celebrate. The love that makes our relationship great. I wish all of your dreams may come true.");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void Update(String s) {
        binding.wishLabel.setText(s);
    }

    @Override
    protected void onDestroy() {
        if (adapter != null){
            adapter.clear();
        }
        super.onDestroy();

    }
}