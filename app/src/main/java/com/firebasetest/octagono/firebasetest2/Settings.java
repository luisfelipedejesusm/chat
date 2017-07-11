package com.firebasetest.octagono.firebasetest2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebasetest.octagono.firebasetest2.Models.User;
import com.firebasetest.octagono.firebasetest2.Util.BitmapToBase64;
import com.firebasetest.octagono.firebasetest2.Util.ImageZoom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class Settings extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private static int RESULT_LOAD_IMAGE = 1009;
    FirebaseDatabase database;
    DatabaseReference myRef;
    User currentUser;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseUser user;
    private StorageReference mStorageRef;
    StorageReference userPhoto;
    Bitmap bitmap;
    ImageView imageView;
    private int mShortAnimationDuration;
    private ImageView thumb1View;
    private ImageView expandedImageView;
    private View container;
    private ImageZoom imageZoom = ImageZoom.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imageView = (ImageView) findViewById(R.id.settings_profile_image);
        thumb1View = (ImageView)findViewById(R.id.settings_profile_image);
        expandedImageView = (ImageView) findViewById(R.id.zoomed_image);
        container = findViewById(R.id.settings_container);

        user = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        userPhoto = mStorageRef
                .child("users")
                .child(user.getEmail().split("@")[0])
                .child("profilePhoto");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(user.getEmail().split("@")[0]);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                loadUserData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void loadUserData(){
        if (currentUser.getFoto64() != null) {
            Glide.with(this)
                    .load(currentUser.getFoto64())
                    .into(imageView);





            thumb1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageZoom.zoomImageFromThumb(thumb1View,expandedImageView,container,Settings.this,currentUser,mShortAnimationDuration);
                }
            });

            mShortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    public void getNewImage(View view){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){

            final Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
                bitmap = null;
            }
            BitmapToBase64 bitmapToBase64 = BitmapToBase64.getInstance();
            String base64Image = bitmapToBase64.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            //currentUser.setFoto64(base64Image);
            userPhoto.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    currentUser.setFoto64(downloadUrl.toString());
                    myRef.setValue(currentUser);
                    //currentUser.setFoto64(downloadUrl);
//                    try {
//
//                    updateImage(imageView,  downloadUrl);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    updateImage();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Settings.this, "No se pudo subir la Imagen", Toast.LENGTH_SHORT).show();
                }
            });
            //bitmap = BitmapFactory.decodeFile("file:///storage/9016-4EF8/Download/Alphonse Elric.png");

        }

    }
    public void updateImage(){

        imageView.setImageBitmap(bitmap);
    }
//    public void updateImage(final ImageView imageView, final StorageReference url) throws IOException {
//        File localFile = File.createTempFile("profileImage","jpg");
//        userPhoto.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//
//               Glide.with(getApplicationContext())
//                       .using(new FirebaseImageLoader())
//                       .load(url)
//                       .into(imageView);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//        //imageView.setImageBitmap(bitmap);
//
//    }


    @Override
    public void onBackPressed() {
        if (imageZoom.isPhotoZoomed()){
            imageZoom.closeZoomedImage(thumb1View,expandedImageView,mShortAnimationDuration);
        }else{
            finish();
        }
    }
}
