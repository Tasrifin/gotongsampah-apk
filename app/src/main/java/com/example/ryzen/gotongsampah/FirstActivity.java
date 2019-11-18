package com.example.ryzen.gotongsampah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        CardView card_market = (CardView) findViewById(R.id.CardView_Market);
        Button btn_donate = (Button) findViewById(R.id.donate_button);
        LinearLayout howitworks = (LinearLayout) findViewById(R.id.viewHowItWork);
        LinearLayout viewuser = (LinearLayout) findViewById(R.id.viewUser_first);

        card_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this, ProductActivity.class);
                startActivity(i);
            }
        });

        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this, DonationActivity.class);
                startActivity(i);
            }
        });

        howitworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this, HowItWorks.class);
                startActivity(i);
            }
        });

        viewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this, DashboardUserActivity.class);
                startActivity(i);
            }
        });
    }


}
