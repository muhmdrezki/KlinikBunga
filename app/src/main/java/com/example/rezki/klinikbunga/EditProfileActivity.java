package com.example.rezki.klinikbunga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton selectImage;
    private EditText etaddress;
    private Button btnsave;
    private TextView tvname;

    private Uri imageuri;

    private static final int GALLERY_REQUEST = 1;

    private DatabaseReference db_login;
    private FirebaseAuth firebaseauth;
    private StorageReference image_storage;
    private FirebaseUser user_Ref;
    private Query user_query;

    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity_profile);

        progressdialog = new ProgressDialog(this);

        db_login = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseauth = FirebaseAuth.getInstance();
        user_Ref = firebaseauth.getCurrentUser();

        String uid = user_Ref.getUid().toString().trim();

        user_query = db_login.equalTo(uid);

        image_storage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        selectImage = (ImageButton) findViewById(R.id.selectImage);
        tvname = (TextView) findViewById(R.id.tvname);
        etaddress = (EditText) findViewById(R.id.etaddress);
        btnsave = (Button) findViewById(R.id.btnsave);

        btnsave.setOnClickListener(this);
        selectImage.setOnClickListener(this);

        firebaseauth = FirebaseAuth.getInstance();
        db_login.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama_user = (String) dataSnapshot.child("name").getValue();
                String alamat_user = (String) dataSnapshot.child("address").getValue();
                String image_user = (String) dataSnapshot.child("image").getValue();

                Picasso.with(EditProfileActivity.this).load(image_user).into(selectImage);
                etaddress.setText(alamat_user);
                tvname.setText(nama_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==selectImage){
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        } else if (view == btnsave){
            startSetupAccount();
        }
    }

    private void startSetupAccount() {

        final String address = etaddress.getText().toString().trim();
        final String userid = firebaseauth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(address)) {
            if (imageuri != null) {
                progressdialog.show();
                progressdialog.setMessage("Saving Profile Data..");

                StorageReference filepath = image_storage.child(imageuri.getLastPathSegment());
                filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String downloadUri = taskSnapshot.getDownloadUrl().toString();

                        db_login.child(userid).child("address").setValue(address);
                        db_login.child(userid).child("image").setValue(downloadUri);

                        progressdialog.dismiss();

                        Intent mainIntent = new Intent(EditProfileActivity.this, ViewProfile.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                        Toast.makeText(EditProfileActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (imageuri == null) {

                db_login.child(userid).child("address").setValue(address);

                progressdialog.dismiss();

                Intent mainIntent = new Intent(EditProfileActivity.this, ViewProfile.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);

                Toast.makeText(EditProfileActivity.this, "Data Updated", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

                Uri imageUri = data.getData();

                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();

                selectImage.setImageURI(imageuri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        final String TAG = this.getClass().getName();
        Log.d(TAG, "click");

        if (doubleBackToExitPressedOnce==true) {
            //super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        doubleBackToExitPressedOnce=true;
        Log.d(TAG, "twice "+ doubleBackToExitPressedOnce);
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                Log.d(TAG, "twice "+ doubleBackToExitPressedOnce);
            }
        }, 3000);
    }
}
