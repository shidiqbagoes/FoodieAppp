package com.w3learnteam.foodieappp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FFoodAdapterr extends RecyclerView.Adapter<FFoodAdapterr.FFoodHolder> {

    Context context;
    List<FFood> arrayList;

    public FFoodAdapterr(Context context,List<FFood>arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FFoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.food_item_checkout,parent,false);
        return new FFoodHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull FFoodHolder holder, int position) {
        final FFood fFood = arrayList.get(position);

        holder.txt_foodname.setText(fFood.getFfoodvalue());
        holder.ffood.setImageDrawable(context.getResources().getDrawable(fFood.getFfoodpic()));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null) ? arrayList.size() : 0;
    }

    public class FFoodHolder extends RecyclerView.ViewHolder {

        private ImageView ffood;
        private TextView txt_foodname;
        private LinearLayout parentlayout;

        public FFoodHolder(@NonNull View itemView) {
            super(itemView);

            ffood = itemView.findViewById(R.id.ffood);
            parentlayout = itemView.findViewById(R.id.parentlayout);
            txt_foodname = itemView.findViewById(R.id.txt_foodname);
        }
    }
}
