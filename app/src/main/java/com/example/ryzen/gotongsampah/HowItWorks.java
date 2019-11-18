package com.example.ryzen.gotongsampah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class HowItWorks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);

        LinearLayout home = (LinearLayout) findViewById(R.id.viewHome);
        LinearLayout viewuser = (LinearLayout) findViewById(R.id.viewUser_first);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HowItWorks.this, FirstActivity.class);
                startActivity(i);
            }
        });

        viewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HowItWorks.this, DashboardUserActivity.class);
                startActivity(i);
            }
        });
    }
}
