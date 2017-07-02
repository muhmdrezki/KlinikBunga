package com.example.rezki.klinikbunga;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SinglePostActivity extends AppCompatActivity {

    private String mPostkey;
    private DatabaseReference databaseReference;

    private TextView title_post;
    private ImageView image_post;
    private TextView desc_post;
    private TextView user_post;
    private TextView category_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_Flower");
        mPostkey = getIntent().getExtras().getString("post_id");

        title_post = (TextView) findViewById(R.id.titlepost);
        desc_post = (TextView) findViewById(R.id.descpost);
        image_post = (ImageView) findViewById(R.id.postimage);
        user_post = (TextView) findViewById(R.id.tvauthor);
        category_post = (TextView) findViewById(R.id.category);

        databaseReference.child(mPostkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Post_Title = (String) dataSnapshot.child("title").getValue();
                String Post_Desc = (String) dataSnapshot.child("description").getValue();
                String category = (String) dataSnapshot.child("category").getValue();
                String Post_image = (String) dataSnapshot.child("image").getValue();
                String Post_uid = (String) dataSnapshot.child("uid").getValue();
                String Post_auth = (String) dataSnapshot.child("username").getValue();

                title_post.setText(Post_Title);
                category_post.setText("Category " + category);
                desc_post.setText(Post_Desc);
                user_post.setText("Posted By " + Post_auth);
                Picasso.with(SinglePostActivity.this).load(Post_image).into(image_post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Toast.makeText(SinglePostActivity.this, post_key, Toast.LENGTH_LONG).show();
    }
}
