package com.example.ryzen.gotongsampah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DonationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private ProgressDialog progressDialog;
    private EditText mJenis,mJumlah,mInfo,mAlamat,mKontak,kodeTel;
    private Spinner satuan;
    private FirebaseUser user;
    private String UserUid;
    private Uri mImageUri;

    private StorageReference mStorageReference;
    private DatabaseReference mDBReference;
    private String tel, berat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);


        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mJenis = findViewById(R.id.jenis);
        mJumlah = findViewById(R.id.banyak);
        mInfo = findViewById(R.id.informasi);
        mKontak = findViewById(R.id.kontakdonasi);
        mAlamat = findViewById(R.id.alamat);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        kodeTel = findViewById(R.id.kode_tel);
        satuan = findViewById(R.id.berat);
        mStorageReference = FirebaseStorage.getInstance().getReference("donation");
        mDBReference = FirebaseDatabase.getInstance().getReference("donation");
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserUid = user.getUid();
        kodeTel.setEnabled(false);

        char x;
        final String [] satuanBerat = {"Kg", "Gram"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DonationActivity.this,android.R.layout.simple_spinner_dropdown_item,satuanBerat);
        satuan.setAdapter(adapter);
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mKontak.getText().toString().startsWith("0")){
                    tel = kodeTel.getText().toString() + mKontak.getText().toString().substring(1);
                }else {
                    tel = kodeTel.getText().toString() + mKontak.getText().toString();
                }

                berat = mJumlah.getText().toString()+ " " +satuan.getSelectedItem().toString();
                Log.v("Berat"," "+berat);
                uploadFile();
            }
        });

    }

    private void clearData() {
        mJenis.setText("");
        mJumlah.setText("");
        mInfo.setText("");
        mKontak.setText("");
        mAlamat.setText("");
        mImageView.setImageBitmap(null);
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(){
        if (mImageUri != null){
            progressDialog = new ProgressDialog(DonationActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            StorageReference fileRef = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

           fileRef.putFile(mImageUri)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Handler handler = new Handler();
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   mProgressBar.setProgress(0);
                                   progressDialog.dismiss();
                                   clearData();
                               }
                           }, 0);

                           Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                           while (!urlTask.isSuccessful());
                           Uri downloadUrl = urlTask.getResult();

                           Toast.makeText(DonationActivity.this,"Upload Donasi Sukses!",Toast.LENGTH_LONG).show();
                           String donateId = mDBReference.push().getKey();
                           ClassDonate donasi = new ClassDonate(mJenis.getText().toString().trim(),berat,mInfo.getText().toString(),tel,mAlamat.getText().toString(),downloadUrl.toString(),UserUid,donateId);
                           mDBReference.child(donateId).setValue(donasi);

                       }
                   })

                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(DonationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                       }
                   })

                   .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                           double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                           mProgressBar.setProgress((int) progress);
                           progressDialog.setProgress((int) progress);
                           progressDialog.show();
                       }
                   });
        }else{
            Toast.makeText(this,"Tidak ada gambar yang dipilih",Toast.LENGTH_SHORT).show();
        }
    }
}
