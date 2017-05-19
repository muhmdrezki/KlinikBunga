package com.example.rezki.klinikbunga;

import android.content.Context;
import android.content.Intent;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RangkaianActivity extends AppCompatActivity {

    private RecyclerView postlist;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener authlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rangkaian);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_Rangkaian");
        databaseReference.keepSynced(true);

        postlist = (RecyclerView) findViewById(R.id.postlist);
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
                FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(

                        Post.class, R.layout.post_row, PostViewHolder.class, databaseReference
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
                                Intent SinglePostIntent = new Intent(RangkaianActivity.this, SinglePostActivity.class);
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
        firebaseauth.addAuthStateListener(authlistener);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add ){

            startActivity(new Intent(RangkaianActivity.this,Post_RangkaianActivity.class));

        } else if ( item.getItemId() == R.id.logout){

            logout();

        } else if ( item.getItemId() == R.id.main_menu ){
            startActivity(new Intent(RangkaianActivity.this, MainMenu.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        firebaseauth.signOut();

    }
}

