package com.w3learnteam.foodieappp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class CompleteSignUp extends AppCompatActivity {

    Button btn_add_photo,btn_save;
    ImageView user_photo;

    MaterialEditText nameinput,addressinput,cardinput;

    Integer max_photo = 1;
    Uri photo_location;

    Animation fade_in_1,fade_in_2;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sign_up);

        getLocalUsername();

        btn_save = findViewById(R.id.btn_save);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        user_photo = findViewById(R.id.user_photo);

        nameinput = findViewById(R.id.nameinput);
        addressinput = findViewById(R.id.addressinput);
        cardinput = findViewById(R.id.cardinput);

        fade_in_1 = AnimationUtils.loadAnimation(CompleteSignUp.this,R.anim.fade_in_1);
        fade_in_2 = AnimationUtils.loadAnimation(CompleteSignUp.this,R.anim.fade_in_2);

        user_photo.startAnimation(fade_in_1);
        btn_add_photo.startAnimation(fade_in_2);

        nameinput.setAlpha(0);
        addressinput.setAlpha(0);
        cardinput.setAlpha(0);
        btn_save.setAlpha(0);

        nameinput.setTranslationX(400);
        addressinput.setTranslationX(400);
        cardinput.setTranslationX(400);
        btn_save.setTranslationY(400);

        nameinput.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(300).start();
        addressinput.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(400).start();
        cardinput.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(500).start();
        btn_save.animate().alpha(1).setDuration(800).translationY(0).setStartDelay(600).start();

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findphoto();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameinput.getText().toString().trim();
                final String address = addressinput.getText().toString().trim();
                final String card = cardinput.getText().toString().trim();

                final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CompleteSignUp.this,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#090037"));
                sweetAlertDialog.setTitle("Loading");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                if(name.equals("") || address.equals("") || card.equals("")){
                    sweetAlertDialog.dismissWithAnimation();

                    SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(CompleteSignUp.this,SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog1.setTitleText("Error");
                    sweetAlertDialog1.setContentText("Field Can't Be Empty");
                    sweetAlertDialog1.setConfirmText("Ok");
                    sweetAlertDialog1.show();

                    Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                    button.setBackgroundResource(R.drawable.btn_select1);
                }
                else if(name.length() < 3){
                    sweetAlertDialog.dismissWithAnimation();
                    nameinput.setError("Fill At Least 3 Characters");
                }
                else if(address.length() < 20){
                    sweetAlertDialog.dismissWithAnimation();
                    addressinput.setError("Fill With Correct Information");
                }
                else if(card.length() < 12){
                    sweetAlertDialog.dismissWithAnimation();
                    cardinput.setError("Fill at Least 12 Number");
                }
                else {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                    storageReference = FirebaseStorage.getInstance().getReference().child("UserPhoto").child(username_key_new);

                    if (photo_location != null){
                        StorageReference storageReference1 =
                                storageReference.child(System.currentTimeMillis() + "." +
                                        getFileExtension(photo_location));




                        storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        sweetAlertDialog.dismissWithAnimation();

                                        String url_photo = uri.toString();

                                        databaseReference.getRef().child("name").setValue(name);
                                        databaseReference.getRef().child("address").setValue(address);
                                        databaseReference.getRef().child("card_number").setValue(card);
                                        databaseReference.getRef().child("url_photo_profile").setValue(url_photo);
                                        databaseReference.getRef().child("bio").setValue("");
                                    }
                                });
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Intent intent = new Intent(CompleteSignUp.this,HomeScreen.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }else{
                        sweetAlertDialog.dismissWithAnimation();

                        SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(CompleteSignUp.this,SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog1.setTitleText("Error");
                        sweetAlertDialog1.setContentText("No Photo Selected!");
                        sweetAlertDialog1.show();

                        Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                        button.setBackgroundResource(R.drawable.btn_select1);
                    }
                }
            }
        });
    }
    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findphoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,max_photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == max_photo && resultCode == RESULT_OK && data != null && data.getData() != null){

            photo_location = data.getData();
            Picasso.get().load(photo_location).fit().centerCrop().into(user_photo);
        }
    }
    public void getLocalUsername(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
