package com.example.rezki.klinikbunga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenu_2_Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView link1, link2, link3;
    private ImageView img1, img2, img3, iv_profile;

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

        img1 = (ImageView) findViewById(R.id.LoveIV);
        img2 = (ImageView) findViewById(R.id.thanksIV);
        img3 = (ImageView) findViewById(R.id.dukaIV);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);

        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        iv_profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==link1){
            startActivity(new Intent(MainMenu_2_Activity.this, HadiahActivity.class));
        } else if (view==link2){
            startActivity(new Intent(MainMenu_2_Activity.this, Hadiah2Activity.class));
        } else if (view==link3){
            startActivity(new Intent(MainMenu_2_Activity.this, Hadiah3Activity.class));
        } else if (view==img1){
            startActivity(new Intent(MainMenu_2_Activity.this, HadiahActivity.class));
        } else if (view==img2){
            startActivity(new Intent(MainMenu_2_Activity.this, Hadiah2Activity.class));
        } else if (view==img3){
            startActivity(new Intent(MainMenu_2_Activity.this, Hadiah3Activity.class));
        } else if (view==iv_profile){
            startActivity(new Intent(MainMenu_2_Activity.this, ViewProfile.class));
        }
    }
}
