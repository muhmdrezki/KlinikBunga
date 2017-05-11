package com.example.rezki.klinikbunga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class FIrstActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnsignin, btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first);

        btnsignin = (Button) findViewById(R.id.btnsignin);
        btnregister = (Button) findViewById(R.id.btnregister);

        btnsignin.setOnClickListener(this);
        btnregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btnsignin){
            startActivity(new Intent(FIrstActivity.this, LoginActivity.class));
        } else if (view==btnregister){
            startActivity(new Intent(FIrstActivity.this, RegisterActivity.class));
        }
    }
}
