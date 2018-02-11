package com.example.kjsce.rms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Button home;
        Button store;

        home = (Button) findViewById(R.id.loc_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regpage = new Intent(location.this, login.class);
                startActivity(regpage);
            }

        });

        store = (Button) findViewById(R.id.loc_store);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regpage = new Intent(location.this,login.class);
                startActivity(regpage);
            }

        });
    }
}
