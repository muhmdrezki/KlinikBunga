package com.example.rezki.klinikbunga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etemail, etpassword, etname;
    private Button btnsignup;
    private TextView signinlink;

    private FirebaseAuth auth;
    private DatabaseReference databasereference;

    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        databasereference = FirebaseDatabase.getInstance().getReference().child("Users");

        progressdialog = new ProgressDialog(this);

        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        etname = (EditText) findViewById(R.id.etname);
        signinlink = (TextView) findViewById(R.id.signinlink);

        btnsignup = (Button) findViewById(R.id.btnsignup);

        btnsignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnsignup){
            startRegister();
        } else if (view == signinlink){
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    }

    private void startRegister() {
        final String name = etname.getText().toString().trim();
        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                progressdialog.show();
                progressdialog.setMessage("Please Wait, Signing Up..");

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            String userid = auth.getCurrentUser().getUid();

                            DatabaseReference current_user  = databasereference.child(userid);
                            current_user.child("Name").setValue(name);
                            current_user.child("image").setValue("Default");

                            progressdialog.dismiss();

                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        }

                    }
                });
        } else {
            progressdialog.dismiss();
            Toast.makeText(RegisterActivity.this, "Please Fill The Form First.", Toast.LENGTH_LONG).show(); }

    }
}