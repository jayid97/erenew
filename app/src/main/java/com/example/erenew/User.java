package com.example.erenew;

public class User {
    private String Name;
    private String Phone;
    private String Email;
    private String UserLevel;


    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    private String imgUri;


    public User (String Email,  String Name,String Phone, String imgUri,String Userlevel)
    {
        this.Email = Email;
        this.Name = Name;
        this.Phone = Phone;
        this.imgUri = imgUri;
        this.UserLevel = Userlevel;
    }


    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }
}
