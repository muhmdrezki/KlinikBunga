package com.example.rezki.klinikbunga;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SearchPost_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_search;
    private RecyclerView post_result;
    private Button btn_find;

    private DatabaseReference postRef;
    private Query search_query;

    private TextView noresult;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_post_);

        postRef = FirebaseDatabase.getInstance().getReference().child("Post_Flower");
        postRef.keepSynced(true);

        String postkey = postRef.getParent().toString();

        Toast.makeText(SearchPost_Activity.this, postkey, Toast.LENGTH_SHORT).show();

        btn_find = (Button) findViewById(R.id.btn_find);
        btn_find.setOnClickListener(this);

        et_search = (EditText) findViewById(R.id.et_search);
    }

    public void cari_post() {
        final String cari = et_search.getText().toString().trim();

        Intent proses_cari = new Intent(SearchPost_Activity.this, Search_Result.class);
        Bundle search_parameter = new Bundle();
        search_parameter.putString("param",cari);
        proses_cari.putExtras(search_parameter);

        startActivity(proses_cari);
    }


    @Override
    public void onClick(View v) {
        if (v == btn_find) {
            cari_post();
        }
    }
}
