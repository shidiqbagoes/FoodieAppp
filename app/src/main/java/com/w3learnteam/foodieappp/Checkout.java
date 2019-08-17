package com.w3learnteam.foodieappp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.shapeofview.ShapeOfView;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Checkout extends AppCompatActivity {

    View btn_back,btn_confirm;
    Button btnminus,btnplus,btn_checkout;
    TextView txt_nama_makanan,txt_harga_makanan,txt_jumlah_makanan,txt_deskripsi_makanan;
    ImageView img_makanan,avatar;
    ShapeOfView circle_avatar;

    RecyclerView recyclerView2;
    FFoodAdapterr adapter;
    List<FFood> list;

    Integer valuejumlahmakanan = 1;
    Integer valuehargamakanan = 0;
    Integer valuetotalharga = 0;

    Random random = new Random();
    Integer randomkey ;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    DatabaseReference reference,reference1,foodprice,userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        randomkey = random.nextInt();

        btn_back = findViewById(R.id.btn_back);
        btnminus = findViewById(R.id.btnminus);
        btnplus = findViewById(R.id.btnplus);
        btn_checkout = findViewById(R.id.btn_checkout);
        btn_confirm = findViewById(R.id.btn_confirm);

        txt_nama_makanan = findViewById(R.id.txt_nama_makanan);
        txt_harga_makanan = findViewById(R.id.txt_harga_makanan);
        txt_jumlah_makanan = findViewById(R.id.txt_jumlah_makanan);
        txt_deskripsi_makanan = findViewById(R.id.txt_deskripsi_makanan);

        img_makanan = findViewById(R.id.img_makanan);
        avatar = findViewById(R.id.avatar);
        circle_avatar = findViewById(R.id.circle_avatar);

        PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X,1.1f);
        PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y,1.1f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(avatar,scalex,scaley);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(800);
        animator.start();

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(circle_avatar,scalex,scaley);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setDuration(800);
        objectAnimator.start();

        recyclerView2 = findViewById(R.id.recyclerView2);

        final SweetAlertDialog pdialog = new SweetAlertDialog(Checkout.this,SweetAlertDialog.PROGRESS_TYPE);
        pdialog.getProgressHelper().setBarColor(Color.parseColor("#090037"));
        pdialog.setTitleText("Loading...");
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        String getuserkey = sharedPreferences.getString(username_key,"");

        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(getuserkey);
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getphoto = dataSnapshot.child("url_photo_profile").getValue().toString();
                Picasso.get().load(getphoto).fit().centerCrop().into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Checkout.this,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText(databaseError.toString());
                sweetAlertDialog.show();

                Button button = sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                button.setBackgroundResource(R.drawable.btn_select1);
            }
        });

        String getffoodvalue = getIntent().getStringExtra("ffoodvalue");

        reference = FirebaseDatabase.getInstance().getReference().child("Makanan").child(getffoodvalue);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nama_makanan = dataSnapshot.child("nama_makanan").getValue().toString();
                String harga_makanan = dataSnapshot.child("harga_makanan").getValue().toString();
                String deskripsi_makanan = dataSnapshot.child("deskpripsi_makanan").getValue().toString();
                String url_gambar = dataSnapshot.child("url").getValue().toString();

                txt_nama_makanan.setText(nama_makanan);
                txt_harga_makanan.setText("IDR "+harga_makanan);
                txt_deskripsi_makanan.setText(deskripsi_makanan);
                Picasso.get().load(url_gambar).fit().centerCrop().into(img_makanan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Checkout.this,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText(databaseError.toString());
                sweetAlertDialog.show();

                Button button = sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                button.setBackgroundResource(R.drawable.btn_select1);
            }
        });

        txt_jumlah_makanan.setText(valuejumlahmakanan.toString());

        if (valuejumlahmakanan < 2){
            btnminus.animate().alpha(0).setDuration(300).start();
            btnminus.setEnabled(false);
        }
        else {
            btnminus.animate().alpha(1).setDuration(300).start();
            btnplus.setEnabled(true);
        }

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

        final String Bakmie = "Bakmie",Bubur = "Bubur",Capcai = "Capcai",
               Jamur = "Jamur",Lumpia = "Lumpia",Puiling = "Puiling";


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView2.setLayoutManager(linearLayoutManager);
        recyclerView2.setHasFixedSize(true);

        adapter = new FFoodAdapterr(Checkout.this,list);
        recyclerView2.setAdapter(adapter);

        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView2);

        if (getffoodvalue.equals(Bakmie)){
            recyclerView2.smoothScrollBy(1,0);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.ViewHolder viewHolderDefault = recyclerView2.
                            findViewHolderForAdapterPosition(0);

                    LinearLayout parentlayoutdef = viewHolderDefault.itemView.
                            findViewById(R.id.parentlayout);
                    parentlayoutdef.animate().scaleY(1f).scaleX(1f).setDuration(550).
                            setInterpolator(new AccelerateInterpolator()).start();
                }
            },100);
        }
        else if(getffoodvalue.equals(Bubur)){
            recyclerView2.smoothScrollBy(1,0);
            linearLayoutManager.scrollToPositionWithOffset(1,120);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    RecyclerView.ViewHolder viewHolderDefault = recyclerView2.
                            findViewHolderForAdapterPosition(1);

                    LinearLayout parentlayoutdef = viewHolderDefault.itemView.
                            findViewById(R.id.parentlayout);
                    parentlayoutdef.animate().scaleY(1f).scaleX(1f).setDuration(550).
                            setInterpolator(new AccelerateInterpolator()).start();
                }
            },100);
        }
        else if(getffoodvalue.equals(Capcai)){
            recyclerView2.smoothScrollBy(1,0);
            linearLayoutManager.scrollToPositionWithOffset(2,120);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.ViewHolder viewHolderr = recyclerView2.findViewHolderForAdapterPosition(2);

                    LinearLayout parentlayout = viewHolderr.itemView.findViewById(R.id.parentlayout);
                    parentlayout.animate().scaleX(1f).scaleY(1f).setDuration(500).setInterpolator(new AccelerateInterpolator()).start();
                }
            },100);
        }
        else if(getffoodvalue.equals(Jamur)){
            recyclerView2.smoothScrollBy(1,0);
            linearLayoutManager.scrollToPositionWithOffset(3,120);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.ViewHolder viewHolderr = recyclerView2.findViewHolderForAdapterPosition(3);

                    LinearLayout parentlayout = viewHolderr.itemView.findViewById(R.id.parentlayout);
                    parentlayout.animate().scaleX(1f).scaleY(1f).setDuration(500).setInterpolator(new AccelerateInterpolator()).start();
                }
            },100);
        }
        else if(getffoodvalue.equals(Lumpia)){
            recyclerView2.smoothScrollBy(1,0);
            linearLayoutManager.scrollToPositionWithOffset(4,120);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.ViewHolder viewHolderr = recyclerView2.findViewHolderForAdapterPosition(4);

                    LinearLayout parentlayout = viewHolderr.itemView.findViewById(R.id.parentlayout);
                    parentlayout.animate().scaleX(1f).scaleY(1f).setDuration(500).setInterpolator(new AccelerateInterpolator()).start();
                }
            },100);
        }
        else if(getffoodvalue.equals(Puiling)){
            recyclerView2.smoothScrollBy(1,0);
            linearLayoutManager.scrollToPositionWithOffset(5,120);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.ViewHolder viewHolderr = recyclerView2.findViewHolderForAdapterPosition(5);

                    LinearLayout parentlayout = viewHolderr.itemView.findViewById(R.id.parentlayout);
                    parentlayout.animate().scaleX(1f).scaleY(1f).setDuration(500).setInterpolator(new AccelerateInterpolator()).start();
                }
            },100);
        }
        else if(getffoodvalue == null){
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Checkout.this,SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitle("Error");
            sweetAlertDialog.setContentText("No Data Acquired");
            sweetAlertDialog.setConfirmText("Close");
            sweetAlertDialog.show();

            Button button = sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM);
            button.setBackgroundResource(R.drawable.btn_select1);
        }

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    View view = snapHelper.findSnapView(linearLayoutManager);
                    int pos = linearLayoutManager.getPosition(view);

                    RecyclerView.ViewHolder viewHolder =
                            recyclerView2.findViewHolderForAdapterPosition(pos);

                    final String food = ((TextView) viewHolder.itemView.findViewById(R.id.txt_foodname)).getText().toString();

                    reference1 = FirebaseDatabase.getInstance().getReference().child("Makanan").child(food);
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String nama_makanan = dataSnapshot.child("nama_makanan").getValue().toString();
                            final String harga_makanan = dataSnapshot.child("harga_makanan").getValue().toString();
                            String deskripsi_makanan = dataSnapshot.child("deskpripsi_makanan").getValue().toString();
                            String url_gambar = dataSnapshot.child("url").getValue().toString();

                            pdialog.dismissWithAnimation();

                            valuehargamakanan = Integer.parseInt(harga_makanan);

                            if(harga_makanan.length() == 5){
                                StringBuffer harga = new StringBuffer(harga_makanan);
                                harga.insert(2,".");

                                txt_harga_makanan.setText("IDR " + harga);
                            }
                            else if(harga_makanan.length() == 6){
                                StringBuffer harga = new StringBuffer(harga_makanan);
                                harga.insert(3,".");

                                txt_harga_makanan.setText("IDR " + harga);
                            }
                            else if(harga_makanan.length() == 7){
                                StringBuffer harga = new StringBuffer(harga_makanan);
                                harga.insert(4,".");

                                txt_harga_makanan.setText("IDR " + harga);
                            }

                            txt_nama_makanan.setText(nama_makanan);
                            txt_deskripsi_makanan.setText(deskripsi_makanan);
                            Picasso.get().load(url_gambar).fit().centerCrop().into(img_makanan);

                            btnplus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    valuejumlahmakanan+=1;
                                    txt_jumlah_makanan.setText(valuejumlahmakanan.toString());
                                    if (valuejumlahmakanan > 1) {
                                        btnminus.animate().alpha(1).setDuration(300).start();
                                        btnminus.setEnabled(true);
                                    }

                                     valuetotalharga = valuehargamakanan * valuejumlahmakanan;
                                     String totalharga = String.valueOf(valuetotalharga);

                                     if(totalharga.length() == 5){
                                        StringBuffer hargatotal = new StringBuffer(totalharga);
                                         hargatotal.insert(2,".");

                                        txt_harga_makanan.setText("IDR " + hargatotal);
                                    }
                                    else if(totalharga.length() == 6){
                                        StringBuffer hargatotal = new StringBuffer(totalharga);
                                        hargatotal.insert(3,".");

                                        txt_harga_makanan.setText("IDR " + hargatotal);
                                    }
                                    else if(totalharga.length() == 7){
                                         StringBuffer hargatotal = new StringBuffer(totalharga);
                                         hargatotal.insert(4,".");
                                         StringBuffer hargatotal1 = new StringBuffer(hargatotal);
                                         hargatotal1.insert(1,".");
                                         txt_harga_makanan.setText("IDR " + hargatotal1);
                                    }
                                }
                            });

                            btnminus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    valuejumlahmakanan-=1;
                                    txt_jumlah_makanan.setText(valuejumlahmakanan.toString());
                                    if (valuejumlahmakanan < 2) {
                                        btnminus.animate().alpha(0).setDuration(300).start();
                                        btnminus.setEnabled(false);
                                    }
                                    valuetotalharga = valuehargamakanan * valuejumlahmakanan;
                                    String totalharga = String.valueOf(valuetotalharga);

                                    if(totalharga.length() == 5){
                                        StringBuffer hargatotal = new StringBuffer(totalharga);
                                        hargatotal.insert(2,".");

                                        txt_harga_makanan.setText("IDR " + hargatotal);
                                    }
                                    else if(totalharga.length() == 6){
                                        StringBuffer hargatotal = new StringBuffer(totalharga);
                                        hargatotal.insert(3,".");

                                        txt_harga_makanan.setText("IDR " + hargatotal);
                                    }
                                    else if(totalharga.length() == 7){
                                        StringBuffer hargatotal = new StringBuffer(totalharga);
                                        hargatotal.insert(4,".");
                                        StringBuffer hargatotal1 = new StringBuffer(hargatotal);
                                        hargatotal1.insert(1,".");

                                        txt_harga_makanan.setText("IDR " + hargatotal1);
                                    }
                                }
                            });

                            btn_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String FoodNames = "You Bought : "+ valuejumlahmakanan +" "+  food;

                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Checkout.this,SweetAlertDialog.SUCCESS_TYPE);
                                    sweetAlertDialog.setTitleText("Added");
                                    sweetAlertDialog.setContentText(FoodNames);
                                    sweetAlertDialog.show();

                                    Button button = sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                                    button.setVisibility(View.GONE);

                                    SharedPreferences sharedPreferences1 = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                    String userkey = sharedPreferences1.getString(username_key,"");

                                    final String boughtkey = userkey + randomkey.toString();

                                    DatabaseReference databaseReferencess = FirebaseDatabase.getInstance().getReference().
                                            child("DataList").child(boughtkey).child(food);

                                    databaseReferencess.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().child("Makanan").setValue(food);
                                            dataSnapshot.getRef().child("Jumlah").setValue(valuejumlahmakanan.toString());
                                            dataSnapshot.getRef().child("Harga").setValue(valuetotalharga);

                                            valuehargamakanan = Integer.parseInt(harga_makanan);
                                            valuetotalharga = valuehargamakanan * valuejumlahmakanan;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            valuejumlahmakanan = 1;
                                            txt_jumlah_makanan.setText("1");
                                            btnminus.animate().alpha(0).setDuration(150).start();
                                        }
                                    });

                                    Handler handlers = new Handler();
                                    handlers.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            DatabaseReference databaseReferencess = FirebaseDatabase.getInstance().getReference()
                                                    .child("DataList").child(boughtkey).child(food);

                                            databaseReferencess.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    },300);

                                }
                            });
                            btn_checkout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent =  new Intent(Checkout.this,Payment.class);
                                    startActivity(intent);
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Checkout.this,SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Error");
                            sweetAlertDialog.setContentText(databaseError.toString());
                            sweetAlertDialog.show();

                            Button button = sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                            button.setBackgroundResource(R.drawable.btn_select1);
                        }
                    });

                    LinearLayout parentlayout = viewHolder.itemView.findViewById(R.id.parentlayout);
                    parentlayout.animate().scaleY(1f).scaleX(1f).setDuration(350).
                            setInterpolator(new AccelerateInterpolator()).start();
                }
                else {

                    View view = snapHelper.findSnapView(linearLayoutManager);
                    int pos = linearLayoutManager.getPosition(view);

                    RecyclerView.ViewHolder viewHolder =
                            recyclerView2.findViewHolderForAdapterPosition(pos);

                    LinearLayout parentlayout = viewHolder.itemView.findViewById(R.id.parentlayout);
                    parentlayout.animate().scaleY(0.75f).scaleX(0.75f).
                            setInterpolator(new AccelerateInterpolator()).setDuration(350).start();

                }


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Checkout.this,Profile.class);
                startActivity(intent);
            }
        });


        }
    }


