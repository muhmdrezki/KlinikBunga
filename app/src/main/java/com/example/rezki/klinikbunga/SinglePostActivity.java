package com.example.rezki.klinikbunga;

import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SinglePostActivity extends AppCompatActivity implements View.OnClickListener{

    private String mPostkey;

    private DatabaseReference databaseReference, userRef;
    private Query user_query;
    private FirebaseAuth auth;
    private FirebaseUser User;

    private TextView title_post;
    private ImageView image_post;
    private TextView desc_post;
    private TextView user_post;
    private TextView category_post;

    private Button btn_delete, btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_Flower");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mPostkey = getIntent().getExtras().getString("post_id");

        auth = FirebaseAuth.getInstance();
        User = auth.getCurrentUser();
        final String userid = User.getUid().toString().trim();

        user_query = userRef.orderByChild("uid");

        title_post = (TextView) findViewById(R.id.titlepost);
        desc_post = (TextView) findViewById(R.id.descpost);
        image_post = (ImageView) findViewById(R.id.postimage);
        user_post = (TextView) findViewById(R.id.tvauthor);
        category_post = (TextView) findViewById(R.id.category);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);

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

                if(userid.equals(Post_uid)){
                    btn_delete.setVisibility(View.VISIBLE);
                    btn_edit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Toast.makeText(SinglePostActivity.this, post_key, Toast.LENGTH_LONG).show();
    }

    public void hapus(){
        databaseReference.child(mPostkey).removeValue();
        startActivity(new Intent(SinglePostActivity.this, MainActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add ){

            startActivity(new Intent(SinglePostActivity.this,PostActivity.class));

        } else if ( item.getItemId() == R.id.logout){

            logout();

        } else if ( item.getItemId() == R.id.main_menu ){
            startActivity(new Intent(SinglePostActivity.this, MainMenu.class));
        } else if (item.getItemId() == R.id.ListBunga ) {
            startActivity(new Intent(SinglePostActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        auth.signOut();

    }

    @Override
    public void onClick(View v) {
        if(v==btn_delete){
            hapus();
        } if(v==btn_edit){
            Intent EditPostIntent = new Intent(SinglePostActivity.this, EditActivity.class);
            EditPostIntent.putExtra("post_id", mPostkey);
            startActivity(EditPostIntent);
        }
    }




}
