package com.example.rezki.klinikbunga;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Hadiah2Activity extends AppCompatActivity {

    private RecyclerView postlist;
    private DatabaseReference databaseReference, likesRef, userRef;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener authlistener;
    private Query QueryDatabase;

    private Boolean like_click = false;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadiah2);

        firebaseauth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_Flower");
        databaseReference.keepSynced(true);

        QueryDatabase = databaseReference.orderByChild("category").equalTo("Tanda Terima Kasih");

        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        likesRef.keepSynced(true);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.keepSynced(true);

        String uid = firebaseauth.getCurrentUser().getUid().toString().trim();

        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama = (String) dataSnapshot.child("name").getValue();
                name = nama;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postlist = (RecyclerView) findViewById(R.id.postlist1);

        postlist.setHasFixedSize(true);
        postlist.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        postlist.setHasFixedSize(true);
        postlist.setLayoutManager(linearLayoutManager);

        firebaseauth = FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>
                        ( Post.class, R.layout.post_row, PostViewHolder.class, QueryDatabase) {

                    @Override
                    protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {

                        final String post_key = getRef(position).getKey();

                        viewHolder.setUsername(String.valueOf(model.getUsername()));
                        viewHolder.setTitle(String.valueOf(model.getTitle()));
                        viewHolder.setDesc(String.valueOf(model.getDescription()));
                        viewHolder.setCategory(String.valueOf(model.getCategory()));
                        viewHolder.setImage(getApplicationContext(), model.getImage());

                        viewHolder.setBtn_like(post_key);

                        viewHolder.btn_like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                like_click = true;
                                likesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (like_click) {
                                            if (dataSnapshot.child(post_key).hasChild(firebaseauth.getCurrentUser().getUid())) {
                                                likesRef.child(post_key).child(firebaseauth.getCurrentUser().getUid()).removeValue();
                                                like_click = false;
                                            } else {
                                                likesRef.child(post_key).child(firebaseauth.getCurrentUser().getUid()).setValue(name);
                                                like_click = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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
        firebaseauth.addAuthStateListener(authlistener);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View view;

        ImageView btn_like;

        DatabaseReference like_db;
        FirebaseAuth auth;


        public PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            btn_like = (ImageView) view.findViewById(R.id.btn_like);
            auth = FirebaseAuth.getInstance();
            like_db = FirebaseDatabase.getInstance().getReference().child("Likes");
            like_db.keepSynced(true);
        }

        public void setBtn_like(final String post_key){
            like_db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid())) {
                        btn_like.setImageResource(R.drawable.redlike);
                    } else {
                        btn_like.setImageResource(R.drawable.greylike);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
    public void onBackPressed() {
        startActivity(new Intent(Hadiah2Activity.this, MainMenu_2_Activity.class));
    }

}
