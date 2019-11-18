package com.example.ryzen.gotongsampah;

public class UserInformation {

    private String name;
    private String email;
    private String phone;
    private String image;


    public UserInformation(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(String image){this.image = image; }
    public String getImage (){return image;}
}
