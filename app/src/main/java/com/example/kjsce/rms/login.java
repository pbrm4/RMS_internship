package com.example.kjsce.rms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button register;
        Button login;

        register = (Button)findViewById(R.id.log_reg);
        login = (Button)findViewById(R.id.log_log);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regpage = new Intent(login.this, register.class);
                startActivity(regpage);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapact = new Intent(login.this,location.class);
                startActivity(mapact);
            }
        });
    }
}
