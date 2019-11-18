package com.example.ryzen.gotongsampah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardUserActivity extends AppCompatActivity {

    private TextView dispnama,dispemail,dispphone;
    private Button btn_logout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRef;
    private FirebaseDatabase mFirebaseDatabase;
    private String UserID;
    private ProgressDialog mProgress;
    CircleImageView imgUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);
        LinearLayout home = (LinearLayout) findViewById(R.id.viewHome);
        LinearLayout howitworks = (LinearLayout) findViewById(R.id.viewHowItWork);

        imgUser = (CircleImageView)findViewById(R.id.imgProfile);
        dispnama=(TextView)findViewById(R.id.txtdispnama);
        dispemail=(TextView)findViewById(R.id.txtdispemail);
        dispphone=(TextView)findViewById(R.id.txtdispphone);
        btn_logout=(Button)findViewById(R.id.btnLogout);
        mAuth=FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference().child("Registration");
        mCurrentUser=mAuth.getCurrentUser();
        UserID=mCurrentUser.getUid();
        mAuthlistener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent i = new Intent(DashboardUserActivity.this,LoginActivity.class);
                    startActivity(i);
                }
            }
        };
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child(UserID).child("email").getValue(String.class);
                String phone = dataSnapshot.child(UserID).child("phone").getValue(String.class);
                String imgPath = dataSnapshot.child(UserID).child("imgUser").getValue(String.class);
                SetDataUser(email,phone,imgPath);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashboardUserActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Keluar ...");
                mProgress.show();
                mAuth.signOut();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (DashboardUserActivity.this, FirstActivity.class);
                startActivity(i);
            }
        });

        howitworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (DashboardUserActivity.this, HowItWorks.class);
                startActivity(i);
            }
        });
    }
    private void SetDataUser(String email, String phone, String imgPath){
        dispemail.setText(email);
        dispphone.setText(phone);
        if(!imgPath.equals("noimage")){
            Picasso.get().load(imgPath).into(imgUser);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthlistener != null){
            mAuth.removeAuthStateListener(mAuthlistener);
        }

    }

    public void Edit(View view) {
        Intent i = new Intent(DashboardUserActivity.this,EditUser.class);
        startActivity(i);
    }

    public void TambahDonasi(View view) {
        Intent i = new Intent(DashboardUserActivity.this,DonationActivity.class);
        startActivity(i);
    }

    public void EditDonasi(View view) {
        Intent i = new Intent(DashboardUserActivity.this,EditDonation.class);
        startActivity(i);
    }

}
