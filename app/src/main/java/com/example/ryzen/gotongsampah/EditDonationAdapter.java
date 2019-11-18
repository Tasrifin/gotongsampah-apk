package com.example.ryzen.gotongsampah;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditDonationAdapter extends RecyclerView.Adapter<EditDonationAdapter.ImageViewHolder>{

    private Context mContext;
    private List<ClassDonate> mUploads;


    public EditDonationAdapter(Context context,List<ClassDonate> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_edit_donation,parent,false);
        return new ImageViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        final ClassDonate donatecurrent = mUploads.get(position);
        holder.tJenis.setText("Jenis     : " + donatecurrent.getdJenis());
        holder.tInfo.setText("Deskripsi : " + donatecurrent.getdInfo());
        Picasso.get()
                .load(donatecurrent.getdImgUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.tHapus.setClickable(true);
        holder.tHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("donation").child(donatecurrent.getDonateUid());
                final StorageReference sRef = FirebaseStorage.getInstance().getReferenceFromUrl(donatecurrent.getdImgUrl());
                AlertDialog.Builder alertBuild = new AlertDialog.Builder(mContext);
                alertBuild.setTitle("Anda yakin ingin menghapus donasi?");
                alertBuild.setMessage("Jenis\t: "+donatecurrent.getdJenis()+"\n"+"Deskripsi\t: "+donatecurrent.getdInfo());
                alertBuild
                        .setCancelable(false)
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    RemoveItem(dRef,sRef,position);
                                    Toast.makeText(mContext,"Donasi berhasil dihapus!",Toast.LENGTH_LONG).show();
                                }catch (Exception e){
                                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                AlertDialog alertDialog = alertBuild.create();
                alertDialog.show();

            }
        });
        holder.tEdit.setClickable(true);
        holder.tEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(mContext, UpdateDonation.class);
                inten.putExtra("donasi",donatecurrent);
                mContext.startActivity(inten);
            }
        });
    }

    private void RemoveItem(DatabaseReference dRef, StorageReference sRef, int pos) {
        mUploads.remove(pos);
        dRef.removeValue();
        sRef.delete();
        this.notifyItemRemoved(pos);
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

       public class ImageViewHolder extends  RecyclerView.ViewHolder{

        public TextView tJenis,tInfo,tEdit, tHapus;

        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            tJenis = itemView.findViewById(R.id.tvJenis);
            tInfo = itemView.findViewById(R.id.tvInfo);
            imageView = itemView.findViewById(R.id.ivDonation);
            tEdit = itemView.findViewById(R.id.tvEdit);
            tHapus = itemView.findViewById(R.id.tvHapus);

        }
    }
}