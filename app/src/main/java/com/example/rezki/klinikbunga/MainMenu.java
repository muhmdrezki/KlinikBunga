package com.example.rezki.klinikbunga;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private ImageView cariIV;
    private TextView  cariTV;
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

                    Intent loginIntent = new Intent(MainMenu.this, LoginActivity.class);
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

        cariIV = (ImageView) findViewById(R.id.cariIV);
        cariTV = (TextView) findViewById(R.id.cariTV);

        cariIV.setOnClickListener(this);
        cariTV.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        if(cariTV == view){
            startActivity(new Intent(MainMenu.this, MainActivity.class));
        } else if (cariIV == view ){
            startActivity(new Intent(MainMenu.this, MainActivity.class));
        }

    }
}
