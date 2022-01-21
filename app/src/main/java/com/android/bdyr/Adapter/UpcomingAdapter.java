package com.android.bdyr.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bdyr.Holder.EventHolder;
import com.android.bdyr.R;

import java.util.ArrayList;

public class UpcomingAdapter extends RecyclerView.Adapter {
    int count=0;
    Context context;
    ArrayList<EventHolder> arrayList;
    int empty=0,not_empty=1;

    public UpcomingAdapter(Context context, ArrayList<EventHolder> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == empty){
            return new Empty(LayoutInflater.from(context).inflate(R.layout.no_event,parent,false));
        }else if (viewType == not_empty){
            return new Not_empty(LayoutInflater.from(context).inflate(R.layout.birthday_row_item,parent,false));
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass() == Empty.class){
            Empty container=(Empty) holder;
        }else if (holder.getClass() == Not_empty.class){
            Not_empty container=(Not_empty) holder;
        }
    }

    @Override
    public int getItemCount() {
        if (arrayList.size() == 0){
            count = -1;
            return count+2;
        }else {
            count=arrayList.size();
            return count;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(count == -1){
            return empty;
        }else {
            return not_empty;
        }
    }
    public static class Empty extends RecyclerView.ViewHolder{

        public Empty(@NonNull View itemView) {
            super(itemView);
        }

    }
    public static class Not_empty extends RecyclerView.ViewHolder{

        public Not_empty(@NonNull View itemView) {
            super(itemView);
        }

    }
}
