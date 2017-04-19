package com.example.rezki.klinikbunga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etemail, etpassword;
    private Button btnsignin;
    private TextView registerlink;

    private FirebaseAuth firebaseauth;
    private DatabaseReference db_login;

    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseauth = FirebaseAuth.getInstance();
        db_login = FirebaseDatabase.getInstance().getReference().child("Users");
        db_login.keepSynced(true);

        progressdialog = new ProgressDialog(this);

        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);

        btnsignin = (Button) findViewById(R.id.btnsignin);
        registerlink = (TextView) findViewById(R.id.registerlink);

        btnsignin.setOnClickListener(this);
        registerlink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == btnsignin){
            checklogin();
        } else if (view==registerlink){
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }

    }

    private void checklogin() {

            String email = etemail.getText().toString().trim();
            String password = etpassword.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            progressdialog.show();
            progressdialog.setMessage("Checking Account, Please Wait..");

            firebaseauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        progressdialog.dismiss();
                        check_userexist();

                    }else {

                        progressdialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Sign In Error", Toast.LENGTH_LONG).show();

                    }
                }
            });

        } else {
            progressdialog.dismiss();
            Toast.makeText(LoginActivity.this, "Please Fill The Form First", Toast.LENGTH_LONG).show(); }
    }

    private void check_userexist(){
        final String userid = firebaseauth.getCurrentUser().getUid();

        db_login.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(userid)){

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                } else {

                    Intent setupIntent = new Intent(LoginActivity.this, ProfileActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
