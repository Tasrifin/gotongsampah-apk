package com.example.ryzen.gotongsampah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DonationAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<ClassDonate> mDonate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDonate = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ClassDonate donate = postSnapshot.getValue(ClassDonate.class);

                    mDonate.add(donate);

                }

                mAdapter =   new DonationAdapter(ProductActivity.this,mDonate);

                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        LinearLayout home = (LinearLayout) findViewById(R.id.viewHome);
        LinearLayout user = (LinearLayout) findViewById(R.id.viewUser_first);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductActivity.this, FirstActivity.class);
                startActivity(i);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductActivity.this, DashboardUserActivity.class);
                startActivity(i);
            }
        });
    }
}
