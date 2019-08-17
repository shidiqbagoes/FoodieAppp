package com.w3learnteam.foodieappp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

public class Edit extends AppCompatActivity {

    MaterialEditText nameedit,addressedit,bioedit,referalinput;
    ImageView edit_user_photo;
    Button btn_edit_photo,btn_save;

    Animation fade_in_1,fade_in_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                ,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_edit);

        nameedit = findViewById(R.id.nameedit);
        addressedit = findViewById(R.id.addressedit);
        bioedit = findViewById(R.id.bioedit);
        referalinput = findViewById(R.id.referalinput);

        edit_user_photo = findViewById(R.id.edit_user_photo);
        btn_edit_photo = findViewById(R.id.btn_edit_photo);
        btn_save = findViewById(R.id.btn_save);

        fade_in_1 = AnimationUtils.loadAnimation(Edit.this,R.anim.fade_in_1);
        fade_in_2 = AnimationUtils.loadAnimation(Edit.this,R.anim.fade_in_2);

        edit_user_photo.startAnimation(fade_in_1);
        btn_edit_photo.startAnimation(fade_in_2);

        nameedit.setAlpha(0);
        addressedit.setAlpha(0);
        bioedit.setAlpha(0);
        referalinput.setAlpha(0);
        btn_save.setAlpha(0);

        nameedit.setTranslationX(400);
        addressedit.setTranslationX(400);
        bioedit.setTranslationX(400);
        referalinput.setTranslationX(400);
        btn_save.setTranslationY(400);

        nameedit.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(300);
        addressedit.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(400);
        bioedit.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(500);
        referalinput.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(600);
        btn_save.animate().alpha(1).translationY(0).setDuration(800).setStartDelay(700);
    }
}
