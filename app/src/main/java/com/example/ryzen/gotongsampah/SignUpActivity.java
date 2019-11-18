package com.example.ryzen.gotongsampah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextView txtName,txtEMail,txtPhone,txtPassword;
    private Button btn_signup;
    private DatabaseReference mDataBase;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtName = (TextView)findViewById(R.id.txtName);
        txtEMail = (TextView)findViewById(R.id.txtEMail);
        txtPhone = (TextView)findViewById(R.id.txtPhone);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
         btn_signup=(Button)findViewById(R.id.btn_regis);
         mProgress = new ProgressDialog(this);

         mDataBase = FirebaseDatabase.getInstance().getReference().child("Registration");
         mAuth = FirebaseAuth.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistration();
            }
        });


    }

    private void startRegistration() {
        final String name=txtName.getText().toString().trim();
        String password=txtPassword.getText().toString().trim();
        final String email=txtEMail.getText().toString().trim();
        final String phone=txtPhone.getText().toString().trim();

        if (!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(password)&& !TextUtils.isEmpty(email)&&!TextUtils.isEmpty(phone)){
            mProgress.setMessage("Mohon Menunggu ...");
            mProgress.show();
            mProgress.setCanceledOnTouchOutside(false);

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    if(user_id != null){
                        DatabaseReference current_user_db = mDataBase.child(user_id);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("email").setValue(email);
                        current_user_db.child("phone").setValue(phone);
                        current_user_db.child("imgUser").setValue("noimage");

                        Intent i = new Intent(SignUpActivity.this,FirstActivity.class);
                        startActivity(i);
                        mProgress.dismiss();
                        Toast.makeText(SignUpActivity.this,"Registrasi Berhasil",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
