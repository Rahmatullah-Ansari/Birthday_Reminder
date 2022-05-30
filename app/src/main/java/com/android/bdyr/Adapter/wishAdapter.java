package com.android.bdyr.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bdyr.Activities.AddEvent;
import com.android.bdyr.R;

import java.util.ArrayList;

public class wishAdapter extends RecyclerView.Adapter<wishAdapter.viewHolder> {
    ArrayList<String> arrayList;
    Context context;
    AddEvent listener;
    boolean checked=false;
    ArrayList<String> temp=new ArrayList<>();
    public wishAdapter(Context context, ArrayList<String> arrayList, AddEvent addEvent) {
        this.arrayList = arrayList;
        this.context = context;
        listener=addEvent;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.wish_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.wishText.setText(arrayList.get(position));
        holder.itemView.setOnLongClickListener(view -> {
            if (temp.contains(arrayList.get(position))){
                temp.remove(arrayList.get(position));
                holder.wishText.setTextColor(Color.WHITE);
                checked=false;
            }else {
                temp.add(arrayList.get(position));
                holder.wishText.setTextColor(Color.RED);
                listener.Update(arrayList.get(position));
                checked=true;
            }
            return true;
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked){
                    if (temp.contains(arrayList.get(position))){
                        temp.remove(arrayList.get(position));
                        holder.wishText.setTextColor(Color.WHITE);
                        checked=false;
                    }else {
                        temp.add(arrayList.get(position));
                        holder.wishText.setTextColor(Color.RED);
                        listener.Update(arrayList.get(position));
                        checked=true;
                    }
                }else{
                    listener.Update(arrayList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void clear() {
        temp.clear();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView wishText;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            wishText=itemView.findViewById(R.id.wish_text);
        }
    }
}
