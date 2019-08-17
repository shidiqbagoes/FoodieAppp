package com.w3learnteam.foodieappp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    Button btn_buy;
    View view;
    RecyclerView recyclerView;
    FFoodAdapter adapter;
    List<FFood> list;
    Context context = HomeScreen.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        view = findViewById(R.id.view);

        btn_buy = findViewById(R.id.btn_buy);
        view.setTranslationX(5);
        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        list.add(
                new FFood("Bakmie",R.drawable.pbakmie)
        );
        list.add(
                new FFood("Bubur",R.drawable.pbubur)
        );

        list.add(
                new FFood("Capcai",R.drawable.pcapcai)
        );
        list.add(
                new FFood("Jamur",R.drawable.pjamur)
        );
        list.add(
                new FFood("Lumpia",R.drawable.plumpia)
        );
        list.add(
                new FFood("Puiling",R.drawable.ppuiling)
        );

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new FFoodAdapter(context,list);
        recyclerView.setAdapter(adapter);

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this,Checkout.class);
                intent.putExtra("ffoodvalue","Bubur");
                startActivity(intent);
            }
        });


    }
}
