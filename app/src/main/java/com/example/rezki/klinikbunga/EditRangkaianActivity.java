package com.example.rezki.klinikbunga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.squareup.picasso.Picasso;

public class EditRangkaianActivity extends AppCompatActivity implements View.OnClickListener {

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

    private String mPostkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mPostkey = getIntent().getExtras().getString("post_id");

        //Tampung ID di Variable
        storage = FirebaseStorage.getInstance().getReference();
        progressdialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_Rangkaian");
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

        databaseReference.child(mPostkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Post_Title = (String) dataSnapshot.child("title").getValue();
                String Post_Desc = (String) dataSnapshot.child("description").getValue();
                String category = (String) dataSnapshot.child("category").getValue();
                String Post_image = (String) dataSnapshot.child("image").getValue();
                String Post_uid = (String) dataSnapshot.child("uid").getValue();
                String Post_auth = (String) dataSnapshot.child("username").getValue();

                posttitle.setText(Post_Title);
                sp_name.setSelected(Boolean.parseBoolean(category));
                postdesc.setText(Post_Desc);
                Picasso.with(EditRangkaianActivity.this).load(Post_image).into(imageselect);
                imageselect.setImageURI(imageURI);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    //Fungsi Posting
    private void StartPosting() {
        //Deklarasi Variable
        final String judul = posttitle.getText().toString().trim();
        final String deskripsi = postdesc.getText().toString().trim();
        final String kategori = sp_name.getSelectedItem().toString().trim();

        //Title, Deskripsi, Gambar gabisa kosong
        //Proses Upload
        //Menampilkan Progress Bar
        if(!kategori.equals("Pilih Kategori")) {
            progressdialog.setMessage("Editing, Please Wait");
            progressdialog.show();
            if (imageURI != null) {
                StorageReference filepath = storage.child("Blog_Images").child(imageURI.getLastPathSegment());
                filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        user_db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                databaseReference.child(mPostkey).child("title").setValue(judul);
                                databaseReference.child(mPostkey).child("description").setValue(deskripsi);
                                databaseReference.child(mPostkey).child("category").setValue(kategori);
                                databaseReference.child(mPostkey).child("image").setValue(downloadUrl.toString());
                                databaseReference.child(mPostkey).child("uid").setValue(user.getUid());
                                databaseReference.child(mPostkey).child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressdialog.dismiss();
                                            startActivity(new Intent(EditRangkaianActivity.this, RangkaianActivity.class));
                                            Toast.makeText(EditRangkaianActivity.this, " Post Edited ", Toast.LENGTH_LONG).show();
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
            } else if (imageURI == null) {
                user_db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.child(mPostkey).child("title").setValue(judul);
                        databaseReference.child(mPostkey).child("description").setValue(deskripsi);
                        databaseReference.child(mPostkey).child("category").setValue(kategori);
                        databaseReference.child(mPostkey).child("uid").setValue(user.getUid());
                        databaseReference.child(mPostkey).child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressdialog.dismiss();
                                    startActivity(new Intent(EditRangkaianActivity.this, RangkaianActivity.class));
                                    Toast.makeText(EditRangkaianActivity.this, " Post Edited ", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } else {
            Toast.makeText(EditRangkaianActivity.this, "Pilih Kategori", Toast.LENGTH_LONG).show();
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


