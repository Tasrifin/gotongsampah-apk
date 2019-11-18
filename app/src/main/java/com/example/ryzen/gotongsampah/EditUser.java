package com.example.ryzen.gotongsampah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUser extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser user;
    private ProgressBar mProgressBar;
    private ProgressDialog progressDialog;
    private CircleImageView imgUser;
    private Uri imgUri;
    private String uid;
    private String imgPath;
    MaterialEditText editName, editPhone;
    private StorageReference mStorageRef;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Registration");
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        editName = (MaterialEditText)findViewById(R.id.editName);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);
        imgUser = (CircleImageView)findViewById(R.id.image_user);
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference("User");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nm = dataSnapshot.child(uid).child("name").getValue(String.class);
                String tlp = dataSnapshot.child(uid).child("phone").getValue(String.class);
                imgPath = dataSnapshot.child(uid).child("imgUser").getValue(String.class);
                SetDataProfile(nm,tlp, imgPath);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void SetDataProfile(String nama, String tlp, String imgPath){
        editName.setText(nama);
        editPhone.setText(tlp);
        if(!imgPath.equals("noimage")){
            Picasso.get().load(imgPath).into(imgUser);
        }
    }
    private void chooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Pilih Foto"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();
            Picasso.get().load(imgUri).into(imgUser);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void UpdateUser(){
        if(imgUri != null){
            progressDialog = new ProgressDialog(EditUser.this);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +"."+ getFileExtension(imgUri));
            fileReference.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                    progressDialog.dismiss();
                                }
                            }, 0);

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            Toast.makeText(EditUser.this,"Update Profile Berhasil!",Toast.LENGTH_LONG).show();
                            mDatabaseRef.child(uid).child("name").setValue(editName.getText().toString());
                            mDatabaseRef.child(uid).child("phone").setValue(editPhone.getText().toString());
                            mDatabaseRef.child(uid).child("imgUser").setValue(downloadUrl.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditUser.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            progressDialog.setProgress((int) progress);
                            progressDialog.show();
                        }
                    });
        }else{
            mDatabaseRef.child(uid).child("name").setValue(editName.getText().toString());
            mDatabaseRef.child(uid).child("phone").setValue(editPhone.getText().toString());
            Toast.makeText(EditUser.this,"Update Profile Berhasil!",Toast.LENGTH_LONG).show();
        }
    }

    public void doUpdateUser(View view) {

        UpdateUser();
        if(!imgPath.equals("noimage")){
            StorageReference mref = FirebaseStorage.getInstance().getReferenceFromUrl(imgPath);
            mref.delete();
        }
    }
}
