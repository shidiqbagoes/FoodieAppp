package com.w3learnteam.foodieappp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    ImageView splash_logo;
    Animation fade_in;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash_screen);

        splash_logo = findViewById(R.id.splash_logo);
        fade_in = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.fade_in);
        splash_logo.startAnimation(fade_in);

        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        String getUser = sharedPreferences.getString(username_key,"");


        if (getUser.isEmpty()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this,SignIn.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }
        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this,HomeScreen.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }
    }
}
