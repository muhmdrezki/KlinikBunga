package com.example.rezki.klinikbunga;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.images.ImageManager;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageselect;
    private static final int GALLERY_REQUEST = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        imageselect = (ImageButton) findViewById(R.id.imageselect);
        imageselect.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
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


