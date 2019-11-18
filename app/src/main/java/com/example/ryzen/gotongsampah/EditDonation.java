package com.example.ryzen.gotongsampah;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EditDonation extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EditDonationAdapter mAdapter;
    private FirebaseUser user;
    private String UserUid;
    private DatabaseReference mDatabaseRef;
    private List<ClassDonate> mDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_donation);

        mRecyclerView = findViewById(R.id.recycler_view_edit_donation);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDonate = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserUid = user.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("donation");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDonate.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String uid = postSnapshot.child("userUid").getValue(String.class);
                    if(uid.equals(UserUid)){
                        ClassDonate donate = postSnapshot.getValue(ClassDonate.class);
                        mDonate.add(donate);
                    }
                }
                mAdapter =   new EditDonationAdapter(EditDonation.this,mDonate);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditDonation.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
