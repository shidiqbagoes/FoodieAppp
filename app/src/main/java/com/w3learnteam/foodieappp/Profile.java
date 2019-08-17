package com.w3learnteam.foodieappp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class Profile extends AppCompatActivity {

    LinearLayout btn_edit;
    Button btn_sign_out;
    TextView username,address,bio;
    ImageView profile_photo,user_bg_photo;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    Integer max_photo = 1;
    Uri photo_location;

    private double latitude;
    private double longtitude;

    DatabaseReference reference,databaseReference;
    StorageReference storageReference;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getLocalUsername();

        btn_edit = findViewById(R.id.btn_edit);
        btn_sign_out = findViewById(R.id.btn_sign_out);

        username = findViewById(R.id.username);
        address = findViewById(R.id.address);
        bio = findViewById(R.id.bio);

        profile_photo = findViewById(R.id.profile_photo);
        user_bg_photo = findViewById(R.id.user_bg_photo);

        final SweetAlertDialog progressdialog = new SweetAlertDialog(Profile.this,SweetAlertDialog.PROGRESS_TYPE);
        progressdialog.getProgressHelper().setBarColor(Color.parseColor("#090037"));
        progressdialog.setCancelable(false);
        progressdialog.show();

       /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }*/
       // Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
       // OnLocationChanged(location);
      //  get_location(location);

        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        String getuserkey = sharedPreferences.getString(username_key,"");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(getuserkey);
        storageReference = FirebaseStorage.getInstance().getReference().child("Users_bg").child(getuserkey);


                if (photo_location != null){
                    StorageReference storageReference1 =
                            storageReference.child(System.currentTimeMillis() +
                                    "." + getFileExtension(photo_location));

                    storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url_photo_bg = uri.toString();

                                    databaseReference.getRef().child("user_bg_photo").setValue(url_photo_bg);
                                }
                            });
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(Profile.this,SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog1.setTitleText("Error");
                            sweetAlertDialog1.setContentText(e.toString());
                            sweetAlertDialog1.show();

                            Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                            button.setBackgroundResource(R.drawable.btn_select1);
                        }
                    });
                }
        user_bg_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findphoto();
            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(getuserkey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getusername = dataSnapshot.child("name").getValue().toString();
                String getbio = dataSnapshot.child("bio").getValue().toString();
                String getphoto = dataSnapshot.child("url_photo_profile").getValue().toString();
                String getaddress = dataSnapshot.child("address").getValue().toString();

                progressdialog.dismissWithAnimation();

                if (getbio.isEmpty()){
                    getbio = "No Bio Yet";
                    bio.setText(getbio);
                }

                bio.setText(getbio);
                address.setText(getaddress);
                username.setText(getusername);
                Picasso.get().load(getphoto).fit().centerCrop().into(profile_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,Edit.class);
                startActivity(intent);
            }
        });
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key,"");
                editor.apply();

                Intent intent = new Intent(Profile.this,SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
            Picasso.get().load(photo_location).fit().into(user_bg_photo);

            if (photo_location != null){
                StorageReference storageReference1 =
                        storageReference.child(System.currentTimeMillis() +
                                "." + getFileExtension(photo_location));

                storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url_photo_bg = uri.toString();

                                databaseReference.getRef().child("user_bg_photo").setValue(url_photo_bg);
                            }
                        });
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(Profile.this,SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog1.setTitleText("Error");
                        sweetAlertDialog1.setContentText(e.toString());
                        sweetAlertDialog1.show();

                        Button button = sweetAlertDialog1.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                        button.setBackgroundResource(R.drawable.btn_select1);
                    }
                });
            }
        }
    }
    public void getLocalUsername(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
