package com.w3learnteam.foodieappp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FFoodAdapter extends RecyclerView.Adapter<FFoodAdapter.FFoodHolder> {

    Context context;
    List<FFood> arrayList;

    public FFoodAdapter(Context context,List<FFood>arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FFoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.food_item,parent,false);
        return new FFoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FFoodHolder holder, int position) {
        final FFood fFood = arrayList.get(position);

        holder.ffood.setImageDrawable(context.getResources().getDrawable(fFood.getFfoodpic()));
        holder.ffood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Checkout.class);
                intent.putExtra("ffoodvalue",fFood.getFfoodvalue());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null) ? arrayList.size() : 0;
    }

    public class FFoodHolder extends RecyclerView.ViewHolder {

        private ImageView ffood;

        public FFoodHolder(@NonNull View itemView) {
            super(itemView);

            ffood = itemView.findViewById(R.id.ffood);
        }
    }
}
