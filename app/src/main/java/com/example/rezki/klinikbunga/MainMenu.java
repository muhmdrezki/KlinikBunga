package com.example.rezki.klinikbunga;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private ImageView cariIV, hadiahIV, rangkaiIV, iv_profile, iv_logout;
    private TextView  cariTV, hadiahTV, rangkaiTV;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener authlistener;
    private DatabaseReference db_login;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Cek apakah user sudah login atau belum
        firebaseauth = FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){

                    Intent loginIntent = new Intent(MainMenu.this, FIrstActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                } else if (firebaseAuth.getCurrentUser()!=null){
                    check_userexist();
                }
            }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_Flower");
        databaseReference.keepSynced(true);
        db_login = FirebaseDatabase.getInstance().getReference().child("Users");
        db_login.keepSynced(true);

        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        iv_logout = (ImageView) findViewById(R.id.iv_logout);

        cariIV = (ImageView) findViewById(R.id.cariIV);
        cariTV = (TextView) findViewById(R.id.cariTV);

        hadiahTV = (TextView) findViewById(R.id.hadiahTV);
        hadiahIV = (ImageView) findViewById(R.id.hadiahIV);

        rangkaiIV = (ImageView) findViewById(R.id.rangkaibungaIV);
        rangkaiTV = (TextView) findViewById(R.id.rangkaibunga);

        cariIV.setOnClickListener(this);
        cariTV.setOnClickListener(this);

        hadiahIV.setOnClickListener(this);
        hadiahTV.setOnClickListener(this);

        rangkaiIV.setOnClickListener(this);
        rangkaiTV.setOnClickListener(this);

        iv_profile.setOnClickListener(this);
        iv_logout.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseauth.addAuthStateListener(authlistener);
    }

    private void check_userexist(){
        final String userid = firebaseauth.getCurrentUser().getUid();
        db_login = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseauth.getCurrentUser().getUid()).child("image");
        final String image = db_login.toString().trim();
        db_login.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(userid) && image=="Default"){

                    Intent mainIntent = new Intent(MainMenu.this, ProfileActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    public void onClick(View view) {
        if(cariTV == view){
            startActivity(new Intent(MainMenu.this, MainActivity.class));
        } else if (cariIV == view ){
            startActivity(new Intent(MainMenu.this, MainActivity.class));
        } else if (hadiahTV == view){
            startActivity(new Intent(MainMenu.this, MainMenu_2_Activity.class));
        } else if (hadiahIV == view){
            startActivity(new Intent(MainMenu.this, MainMenu_2_Activity.class));
        } else if (rangkaiTV == view){
            startActivity(new Intent(MainMenu.this, RangkaianActivity.class));
        } else if (rangkaiIV == view){
            startActivity(new Intent(MainMenu.this, RangkaianActivity.class));
        } else if (iv_profile == view) {
            startActivity(new Intent(MainMenu.this, ViewProfile.class));
        } else if (iv_logout == view){
            firebaseauth.signOut();
        }
    }
}
