package com.example.ryzen.gotongsampah;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ImageViewHolder>{

    private Context mContext;
    private List<ClassDonate> mUploads;

    public DonationAdapter(Context context,List<ClassDonate> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        ClassDonate donatecurrent = mUploads.get(position);
        holder.tJenis.setText("Jenis : " + donatecurrent.getdJenis());
        holder.tAlamat.setText("Alamat : " + donatecurrent.getdAlamat());
        holder.tInfo.setText("Deskripsi : " + donatecurrent.getdInfo());
        holder.tJumlah.setText("Berat : " + donatecurrent.getdJumlah());
        holder.tKontak.setText("Whatsapp : " + donatecurrent.getdKontak());
        holder.btnHubDonatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String pesan = "Hallo, saya ingin mengambil donasi sampah mu :)";
                    String tlp = holder.tKontak.getText().toString();
                    Intent openWhatsapp = new Intent(Intent.ACTION_VIEW);
                    openWhatsapp.setData(Uri.parse("https://wa.me/"+tlp+"/?text="+pesan));
                    mContext.startActivities(new Intent[]{openWhatsapp});
                }catch(Exception e){
                    Toast.makeText(mContext,"Whatsapp Belum Terinstall!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Picasso.get()
                .load(donatecurrent.getdImgUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends  RecyclerView.ViewHolder{

        public TextView tJenis,tJumlah,tKontak,tInfo,tAlamat;
        public Button btnHubDonatur;

        public ImageView imageView;


        public ImageViewHolder(View itemView) {
            super(itemView);

            tJenis = itemView.findViewById(R.id.djenis);
            tJumlah = itemView.findViewById(R.id.djumlah);
            tKontak = itemView.findViewById(R.id.dkontak);
            tInfo = itemView.findViewById(R.id.dinfo);
            tAlamat = itemView.findViewById(R.id.dalamat);
            imageView = itemView.findViewById(R.id.donation_image_display);
            btnHubDonatur = itemView.findViewById(R.id.btn_hub_donatur);

        }
    }
}
