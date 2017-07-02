package com.example.rezki.klinikbunga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainMenu_2_Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView link1, link2, link3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_2_);

        link1 = (TextView) findViewById(R.id.LoveTV);
        link2 = (TextView) findViewById(R.id.thanksTV);
        link3 = (TextView) findViewById(R.id.dukaTV);

        link1.setOnClickListener(this);
        link2.setOnClickListener(this);
        link3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==link1){
            startActivity(new Intent(MainMenu_2_Activity.this, HadiahActivity.class));
        } else if (view==link2){
            startActivity(new Intent(MainMenu_2_Activity.this, Hadiah2Activity.class));
        } else if (view==link3){
            startActivity(new Intent(MainMenu_2_Activity.this, Hadiah3Activity.class));
        }
    }
}
