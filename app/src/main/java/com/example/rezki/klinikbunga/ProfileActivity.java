package com.example.rezki.klinikbunga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.net.URI;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton selectImage;
    private EditText etname, etaddress;
    private Button btnsave;
    private TextView backlink;

    private Uri imageuri;

    private static final int GALLERY_REQUEST = 1;

    private DatabaseReference db_login;
    private FirebaseAuth firebaseauth;
    private StorageReference image_storage;
    private FirebaseUser user_Ref;

    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressdialog = new ProgressDialog(this);

        db_login = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseauth = FirebaseAuth.getInstance();
        user_Ref = firebaseauth.getCurrentUser();
        String uid = user_Ref.toString().trim();


        image_storage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        selectImage = (ImageButton) findViewById(R.id.selectImage);
        etname = (EditText) findViewById(R.id.etname);
        etaddress = (EditText) findViewById(R.id.etaddress);
        btnsave = (Button) findViewById(R.id.btnsave);

        btnsave.setOnClickListener(this);
        selectImage.setOnClickListener(this);
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
        } else if (view == backlink){
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        }
    }

    private void startSetupAccount() {

        final String name = etname.getText().toString().trim();
        final String address = etaddress.getText().toString().trim();
        final String userid = firebaseauth.getCurrentUser().getUid();

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address) && imageuri!=null){

                progressdialog.show();
                progressdialog.setMessage("Saving Profile Data..");

                StorageReference filepath = image_storage.child(imageuri.getLastPathSegment());
                filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                       String downloadUri = taskSnapshot.getDownloadUrl().toString();

                        db_login.child(userid).child("name").setValue(name);
                        db_login.child(userid).child("address").setValue(address);
                        db_login.child(userid).child("image").setValue(downloadUri);

                        progressdialog.dismiss();

                        Intent mainIntent = new Intent(ProfileActivity.this, MainMenu.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                        Toast.makeText(ProfileActivity.this, "Data Saved", Toast.LENGTH_LONG).show();
                    }
                });
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
