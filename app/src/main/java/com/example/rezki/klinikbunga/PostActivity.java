package com.example.rezki.klinikbunga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText posttitle;
    private EditText postdesc;

    private Button buttonsubmit;
    private ImageButton imageselect;

    private Uri imageURI = null;


    private static final int GALLERY_REQUEST = 1;
    private StorageReference storage;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Tampung ID di Variable
        storage = FirebaseStorage.getInstance().getReference();
        progressdialog = new ProgressDialog(this);

        imageselect     = (ImageButton) findViewById(R.id.imageselect);
        posttitle       = (EditText) findViewById(R.id.posttitle);
        postdesc        = (EditText) findViewById(R.id.postdesc);
        buttonsubmit    = (Button) findViewById(R.id.buttonsubmit);

        //Supaya Tombol/Link Berfungsi
        imageselect.setOnClickListener(this);
        buttonsubmit.setOnClickListener(this);
    }


    //Fungsi Posting
    private void StartPosting() {
        //Deklarasi Variable
        String judul = posttitle.getText().toString().trim();
        String deskripsi = postdesc.getText().toString().trim();

        //Menampilkan Progress Bar
        progressdialog.setMessage("Posting, Please Wait");
        progressdialog.show();

        //Title, Deskripsi, Gambar gabisa kosong
        if(!TextUtils.isEmpty(judul) && !TextUtils.isEmpty(deskripsi) && imageURI != null){
        //Proses Upload
            StorageReference filepath = storage.child("Blog_Images").child(imageURI.getLastPathSegment());

            filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    progressdialog.dismiss();
                }
            });
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
            Uri imageUri = data.getData();
            imageselect.setImageURI(imageUri);
        }
    }
}


