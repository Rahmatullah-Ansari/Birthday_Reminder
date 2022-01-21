package com.android.bdyr.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bdyr.Holder.EventHolder;
import com.android.bdyr.R;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<EventHolder> arrayList;
    int count=0,empty=1,not_empty=2;

    public EventListAdapter(Context context, ArrayList<EventHolder> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == empty){
            return new Empty(LayoutInflater.from(context).inflate(R.layout.no_event,parent,false));
        }else if (viewType == not_empty){
            return new Empty(LayoutInflater.from(context).inflate(R.layout.birthday_row_item,parent,false));
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass() == Empty.class){
            Empty container=(Empty) holder;
            container.textView.setText("No event are stored now!\nAdd new event by clicking + button\nor pull down to refresh!");
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
        if (count == -1){
            return empty;
        }else {
            return not_empty;
        }
    }
    public static class Empty extends RecyclerView.ViewHolder{
        TextView textView;
        public Empty(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.no_event_text);
        }

    }
    public static class Not_empty extends RecyclerView.ViewHolder{

        public Not_empty(@NonNull View itemView) {
            super(itemView);
        }

    }
}
