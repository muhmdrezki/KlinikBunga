package com.example.rezki.klinikbunga;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private RecyclerView postlist;
    private DatabaseReference databaseReference;
    private DatabaseReference db_login;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener authlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cek apakah user sudah login atau belum
        firebaseauth = FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){

                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_Flower");
        databaseReference.keepSynced(true);
        db_login = FirebaseDatabase.getInstance().getReference().child("Users");
        db_login.keepSynced(true);

        postlist = (RecyclerView) findViewById(R.id.postlist);
        postlist.setHasFixedSize(true);
        postlist.setLayoutManager(new LinearLayoutManager(this));

        check_userexist();

    }

    @Override
    protected void onStart() {
        super.onStart();


        firebaseauth.addAuthStateListener(authlistener);
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(

            Post.class,R.layout.post_row,PostViewHolder.class,databaseReference
        )   {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {

                viewHolder.setTitle(String.valueOf(model.getTitle()));
                viewHolder.setDesc(String.valueOf(model.getDescription()));
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }
        };
            postlist.setAdapter(firebaseRecyclerAdapter);
    }

    private void check_userexist(){
        final String userid = firebaseauth.getCurrentUser().getUid();

        db_login.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(userid)){

                    Intent mainIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View view;

        public PostViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }

        public void setTitle(String title){
            TextView titlepost = (TextView) view.findViewById(R.id.titlepost);
            titlepost.setText(title);
        }

        public void setDesc(String description){
            TextView descpost = (TextView) view.findViewById(R.id.descpost);
            descpost.setText(description);
        }

        public void setImage(Context ctx, String image){
           ImageView postimage = (ImageView) view.findViewById(R.id.postimage);
            Picasso.with(ctx).load(image).into(postimage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add){

                startActivity(new Intent(MainActivity.this,PostActivity.class));

        } else if ( item.getItemId() == R.id.logout){

                logout();

        } else if (item.getItemId() == R.id.seeprofile){

                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        firebaseauth.signOut();

    }
}
