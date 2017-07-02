package com.example.rezki.klinikbunga;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.squareup.picasso.Picasso;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_profile;
    private TextView tv_address, tv_nama;
    private Button btn_Edit;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Query QueryDatabase;
    private FirebaseAuth.AuthStateListener authlistener;

    private DatabaseReference db_user, db_post;
    private StorageReference storage_Ref;

    private RecyclerView postlist;

    private String nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String uid = user.getUid().toString().trim();

        db_post = FirebaseDatabase.getInstance().getReference().child("Post_Flower");
        db_post.keepSynced(true);

        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_nama = (TextView) findViewById(R.id.tv_nama);
        btn_Edit = (Button) findViewById(R.id.buttonedit);

        btn_Edit.setOnClickListener(this);

        db_user = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        storage_Ref = FirebaseStorage.getInstance().getReference().child("Profile_images");

        auth = FirebaseAuth.getInstance();
        db_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String nama_user = (String) dataSnapshot.child("name").getValue();
                String alamat_user = (String) dataSnapshot.child("address").getValue();
                String image_user = (String) dataSnapshot.child("image").getValue();

                Picasso.with(ViewProfile.this).load(image_user).into(iv_profile);
                tv_address.setText(alamat_user);
                tv_nama.setText(nama_user);
                nama = nama_user;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(ViewProfile.this, uid, Toast.LENGTH_LONG ).show();
        postlist = (RecyclerView) findViewById(R.id.post_list1);

        postlist.setHasFixedSize(true);
        postlist.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        postlist.setHasFixedSize(true);
        postlist.setLayoutManager(linearLayoutManager);

        QueryDatabase = db_post.orderByChild("uid").equalTo(uid);

        auth = FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(

                        Post.class, R.layout.post_row, PostViewHolder.class, QueryDatabase
                ) {
                    @Override
                    protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {

                        final String post_key = getRef(position).getKey();

                        viewHolder.setUsername(String.valueOf(model.getUsername()));
                        viewHolder.setTitle(String.valueOf(model.getTitle()));
                        viewHolder.setDesc(String.valueOf(model.getDescription()));
                        viewHolder.setCategory(String.valueOf(model.getCategory()));
                        viewHolder.setImage(getApplicationContext(), model.getImage());

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_LONG).show();
                                Intent SinglePostIntent = new Intent(ViewProfile.this, SinglePostActivity.class);
                                SinglePostIntent.putExtra("post_id", post_key);
                                startActivity(SinglePostIntent);
                            }
                        });
                    }
                };
                postlist.setAdapter(firebaseRecyclerAdapter);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authlistener);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View view;

        public PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setUsername(String username) {
            TextView tvauthor = (TextView) view.findViewById(R.id.tvauthor);
            tvauthor.setText("Posted By " + username);
        }

        public void setTitle(String title) {
            TextView titlepost = (TextView) view.findViewById(R.id.titlepost);
            titlepost.setText(title);
        }

        public void setDesc(String description) {
            TextView descpost = (TextView) view.findViewById(R.id.descpost);
            descpost.setText(description);
        }

        public void setCategory(String category) {
            TextView descpost = (TextView) view.findViewById(R.id.category);
            descpost.setText("Category " + category);
        }

        public void setImage(Context ctx, String image) {
            ImageView postimage = (ImageView) view.findViewById(R.id.postimage);
            Picasso.with(ctx).load(image).into(postimage);
        }
    }

    @Override
    public void onClick(View view) {
        if(view==btn_Edit){
            startActivity(new Intent(ViewProfile.this, ProfileActivity.class));
        }
    }
}
