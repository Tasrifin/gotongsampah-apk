package com.example.ryzen.gotongsampah;

import android.os.Parcel;
import android.os.Parcelable;

public class ClassDonate implements Parcelable {
    private String dJenis;
    private String dJumlah;
    private String dInfo;
    private String dKontak;
    private String dAlamat;
    private String dImgUrl;
    private String dUserUid;
    private String dDonateUid;

    public ClassDonate(String jenis,String jumlah, String info, String kontak, String alamat, String imgurl, String userUid, String donateUid){
        dJenis = jenis;
        dJumlah = jumlah;
        dInfo = info;
        dKontak = kontak;
        dAlamat = alamat;
        dImgUrl = imgurl;
        dUserUid = userUid;
        dDonateUid = donateUid;
    }

    public String getdJenis() {
        return dJenis;
    }
    public void setdJenis(String dJenis) {
        this.dJenis = dJenis;
    }

    public String getdJumlah() {
        return dJumlah;
    }
    public void setdJumlah(String dJumlah) {
        this.dJumlah = dJumlah;
    }

    public String getdInfo() {
        return dInfo;
    }
    public void setdInfo(String dInfo) {
        this.dInfo = dInfo;
    }

    public String getdKontak() {
        return dKontak;
    }
    public void setdKontak(String dKontak) {
        this.dKontak = dKontak;
    }

    public String getdAlamat() {
        return dAlamat;
    }
    public void setdAlamat(String dAlamat) {
        this.dAlamat = dAlamat;
    }

    public String getdImgUrl() {
        return dImgUrl;
    }
    public void setdImgUrl(String dImgUrl) {
        this.dImgUrl = dImgUrl;
    }

    public String getUserUid(){return dUserUid;}
    public void setdUserUid(String userUid){this.dUserUid = userUid;}

    public String getDonateUid(){return dDonateUid;}
    public void setDonateUid(String donateUid){this.dDonateUid = donateUid;}

    public ClassDonate(){

    }



    protected ClassDonate(Parcel in) {
        dJenis = in.readString();
        dJumlah = in.readString();
        dInfo = in.readString();
        dKontak = in.readString();
        dAlamat = in.readString();
        dImgUrl = in.readString();
        dUserUid = in.readString();
        dDonateUid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dJenis);
        dest.writeString(dJumlah);
        dest.writeString(dInfo);
        dest.writeString(dKontak);
        dest.writeString(dAlamat);
        dest.writeString(dImgUrl);
        dest.writeString(dUserUid);
        dest.writeString(dDonateUid);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ClassDonate> CREATOR = new Parcelable.Creator<ClassDonate>() {
        @Override
        public ClassDonate createFromParcel(Parcel in) {
            return new ClassDonate(in);
        }

        @Override
        public ClassDonate[] newArray(int size) {
            return new ClassDonate[size];
        }
    };
}
