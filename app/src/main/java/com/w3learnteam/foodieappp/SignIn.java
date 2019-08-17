package com.w3learnteam.foodieappp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    TextView register,textView5;
    Button btn_sign_in;
    MaterialEditText inputusername,inputpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                ,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_sign_in);

        register = findViewById(R.id.register);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        textView5 = findViewById(R.id.textView5);

        inputusername = findViewById(R.id.inputusername);
        inputpassword = findViewById(R.id.inputpassword);

        inputusername.setAlpha(0);
        inputpassword.setAlpha(0);
        textView5.setAlpha(0);
        register.setAlpha(0);
        btn_sign_in.setAlpha(0);

        inputusername.setTranslationX(400);
        inputpassword.setTranslationX(400);
        textView5.setTranslationY(400);
        register.setTranslationY(400);
        btn_sign_in.setTranslationY(400);

        inputusername.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(100).start();
        inputpassword.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(200).start();
        textView5.animate().alpha(1).translationY(0).setDuration(800).setStartDelay(400).start();
        register.animate().alpha(1).translationY(0).setDuration(800).setStartDelay(500).start();
        btn_sign_in.animate().alpha(1).translationY(0).setDuration(800).setStartDelay(300).start();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
            }
        });
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SignIn.this,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#090037"));
                sweetAlertDialog.setTitleText("Loading");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                final String username = inputusername.getText().toString().trim();
                final String password = inputpassword.getText().toString().trim();

                if (username.equals("") || password.equals("")){
                    sweetAlertDialog.dismissWithAnimation();
                    SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(SignIn.this,SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog1.setTitleText("Error");
                    sweetAlertDialog1.setContentText("Fill All Field");
                    sweetAlertDialog1.show();

                    Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                    button.setBackgroundResource(R.drawable.btn_select1);
                }
                else {
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                String checkpassword = dataSnapshot.child("password").getValue().toString();

                                if (password.equals(checkpassword)){
                                    sweetAlertDialog.dismissWithAnimation();

                                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(username_key,username);
                                    editor.apply();

                                    Intent intent = new Intent(SignIn.this,HomeScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    sweetAlertDialog.dismissWithAnimation();
                                    SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(SignIn.this,SweetAlertDialog.ERROR_TYPE);
                                    sweetAlertDialog1.setTitleText("Error");
                                    sweetAlertDialog1.setContentText("Wrong Password!");
                                    sweetAlertDialog1.show();

                                    Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                                    button.setBackgroundResource(R.drawable.btn_select1);
                                }
                            }
                            else {
                                sweetAlertDialog.dismissWithAnimation();
                                SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(SignIn.this,SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog1.setTitleText("Error");
                                sweetAlertDialog1.setContentText("No Username Registered");
                                sweetAlertDialog1.show();

                                Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                                button.setBackgroundResource(R.drawable.btn_select1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            sweetAlertDialog.dismissWithAnimation();

                            SweetAlertDialog sweetAlertDialog2 = new SweetAlertDialog(SignIn.this,SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog2.setTitle("Error");
                            sweetAlertDialog2.setContentText("No Data Acquired");
                            sweetAlertDialog2.setConfirmText("Close");
                            sweetAlertDialog2.show();

                            Button button = sweetAlertDialog2.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                            button.setBackgroundResource(R.drawable.btn_select1);
                        }
                    });
                }
            }
        });
    }
}
