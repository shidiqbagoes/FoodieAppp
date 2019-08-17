package com.w3learnteam.foodieappp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    Button btn_sign_up,btn_sign_in;
    MaterialEditText inputnewusername,inputnewemail,inputnewpassword,inputrepeatpassword;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseApp.initializeApp(this);

        inputnewusername = findViewById(R.id.inputnewusername);
        inputnewemail = findViewById(R.id.inputnewemail);
        inputnewpassword = findViewById(R.id.inputnewpassword);
        inputrepeatpassword = findViewById(R.id.inputrepeatpassword);

        btn_sign_up = findViewById(R.id.btn_sign_up);
        btn_sign_in = findViewById(R.id.btn_sign_in);

        inputnewusername.setAlpha(0);
        inputnewemail.setAlpha(0);
        inputnewpassword.setAlpha(0);
        inputrepeatpassword.setAlpha(0);
        btn_sign_in.setAlpha(0);
        btn_sign_up.setAlpha(0);

        inputnewusername.setTranslationX(400);
        inputnewemail.setTranslationX(400);
        inputnewpassword.setTranslationX(400);
        inputrepeatpassword.setTranslationX(400);
        btn_sign_in.setTranslationY(400);
        btn_sign_up.setTranslationY(400);

        inputnewusername.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(100).start();
        inputnewemail.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(200).start();
        inputnewpassword.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(300).start();
        inputrepeatpassword.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(400).start();
        btn_sign_in.animate().alpha(1).translationY(0).setDuration(800).setStartDelay(500).start();
        btn_sign_up.animate().alpha(1).translationY(0).setDuration(800).setStartDelay(600).start();

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = inputnewusername.getText().toString().trim();
                final String email = inputnewemail.getText().toString().trim();
                final String password = inputnewpassword.getText().toString().trim();
                final String reppassword = inputrepeatpassword.getText().toString().trim();

                final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SignUp.this,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#090037"));
                sweetAlertDialog.setTitle("Loading");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                if(username.equals("") || email.equals("") || password.equals("") || reppassword.equals("")){
                    sweetAlertDialog.dismissWithAnimation();

                    SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(SignUp.this,SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog1.setTitleText("Error");
                    sweetAlertDialog1.setContentText("All Field Must Be Filled");
                    sweetAlertDialog1.setConfirmText("Ok");
                    sweetAlertDialog1.show();

                    Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                    button.setBackgroundResource(R.drawable.btn_select1);
                }
                else if(!password.equals(reppassword)){
                    sweetAlertDialog.dismissWithAnimation();

                    SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(SignUp.this,SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog1.setTitleText("Error");
                    sweetAlertDialog1.setContentText("Password Mismatch");
                    sweetAlertDialog1.setConfirmText("Ok");
                    sweetAlertDialog1.show();

                    Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                    button.setBackgroundResource(R.drawable.btn_select1);
                }
                else if (username.length() < 6 || email.length() < 6 || password.length() < 6 || inputrepeatpassword.toString().length() < 6){
                    sweetAlertDialog.dismissWithAnimation();

                    SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(SignUp.this,SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog1.setTitleText("Error");
                    sweetAlertDialog1.setContentText("Fill At Least 6 Characters");
                    sweetAlertDialog1.setConfirmText("Ok");
                    sweetAlertDialog1.show();

                    Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                    button.setBackgroundResource(R.drawable.btn_select1);
                }

                else {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                sweetAlertDialog.dismissWithAnimation();

                                SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(SignUp.this,SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog1.setTitleText("Error");
                                sweetAlertDialog1.setContentText("Username Already Exist");
                                sweetAlertDialog1.show();

                                Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                                button.setBackgroundResource(R.drawable.btn_select1);
                            }
                            else {
                                sweetAlertDialog.dismissWithAnimation();

                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key,username);
                                editor.apply();

                                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        sweetAlertDialog.dismissWithAnimation();

                                        dataSnapshot.getRef().child("username").setValue(username);
                                        dataSnapshot.getRef().child("email").setValue(email);
                                        dataSnapshot.getRef().child("password").setValue(password);
                                        dataSnapshot.getRef().child("balance").setValue(500000);

                                        Intent intent = new Intent(SignUp.this,CompleteSignUp.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        sweetAlertDialog.dismissWithAnimation();

                                        SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(SignUp.this,SweetAlertDialog.WARNING_TYPE);
                                        sweetAlertDialog1.setTitleText("Warning");
                                        sweetAlertDialog1.setContentText("Check Your Internet Connection!");
                                        sweetAlertDialog1.setConfirmText("Ok");
                                        sweetAlertDialog1.show();

                                        Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                                        button.setBackgroundResource(R.drawable.btn_select1);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
