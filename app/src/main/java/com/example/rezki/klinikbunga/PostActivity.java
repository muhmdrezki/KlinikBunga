package com.example.rezki.klinikbunga;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import org.w3c.dom.Text;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText posttitle;
    private EditText postdesc;

    private Button buttonsubmit;
    private ImageButton imageselect;

    private Spinner sp_name;

    private Uri imageURI;

    private static final int GALLERY_REQUEST = 1;
    private StorageReference storage;
    private ProgressDialog progressdialog;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Tampung ID di Variable
        storage = FirebaseStorage.getInstance().getReference();
        progressdialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_Flower");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        imageselect     = (ImageButton) findViewById(R.id.imageselect);
        posttitle       = (EditText) findViewById(R.id.posttitle);
        postdesc        = (EditText) findViewById(R.id.postdesc);
        buttonsubmit    = (Button) findViewById(R.id.buttonsubmit);
        sp_name         = (Spinner) findViewById(R.id.sp_name);

        //Supaya Tombol/Link Berfungsi
        imageselect.setOnClickListener(this);
        buttonsubmit.setOnClickListener(this);
    }


    //Fungsi Posting
    private void StartPosting() {
        //Deklarasi Variable
        final String judul = posttitle.getText().toString().trim();
        final String deskripsi = postdesc.getText().toString().trim();
        final String kategori = sp_name.getSelectedItem().toString().trim();

        //Title, Deskripsi, Gambar gabisa kosong
        if(!TextUtils.isEmpty(judul) && !TextUtils.isEmpty(deskripsi) && !kategori.equals("Pilih Kategori") && imageURI != null){
            //Proses Upload
            //Menampilkan Progress Bar
            progressdialog.setMessage("Posting, Please Wait");
            progressdialog.show();
            StorageReference filepath = storage.child("Blog_Images").child(imageURI.getLastPathSegment());
            filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //Database Push
                    final DatabaseReference newPost = databaseReference.push();
                    user_db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(judul);
                            newPost.child("description").setValue(deskripsi);
                            newPost.child("category").setValue(kategori);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(user.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressdialog.dismiss();
                                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                                        Toast.makeText(PostActivity.this, " Upload Success ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }else{
            progressdialog.dismiss();
            Toast.makeText(this, "Can't post, make sure you fill all the blanks",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
          if(imageselect==view)
        {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        } else if(buttonsubmit==view){
            StartPosting();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            imageURI = data.getData();
            imageselect.setImageURI(imageURI);
        }
    }

}


