package com.example.notmyapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class user implements Serializable {
    private String name;
    private String pass;
    private String email;
    private String key;
    private ArrayList<Meds> medlist;
    private String pic;
    private String Phone;


    public user(String name,String pass, String email, String Phone, String pic,String key ){
    this.name=name;
    this.pass=pass;
    this.email=email;
    this.Phone=Phone;
    this.pic=pic;
    this.key=key;



}
public user(){
    this.name=null;
    this.pass=null;
    this.email=null;
    this.Phone=null;
    this.pic=null;
    this.key=null;
    this.medlist=null;
    this.pic=null;
    this.Phone=null;


}
    public String getUserName() {
        return name;
    }

    public String getPassWord() {
        return pass;
    }

    public String getEmail() {
        return this.email;
    }

    public String getKey() {
        return this.key;
    }

    public ArrayList<Meds>getMedlist() {
        return medlist;
    }



    public String getPhone() {
        return Phone;
    }



    public void setUserName(String userName) {
        this.name = userName;
    }

    public void setPassWord(String passWord) {
        this.pass = passWord;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMedlist(ArrayList<Meds> medlist) {
        this.medlist = medlist;
    }
    public void addToMedlist(Meds m) {
        this.medlist.add(m);
    }



    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }






    @Override
    public String toString()
    {
        return "com.example.databasework.user{" +
                "UserName='" + name + '\'' +
                ", PassWord='" + pass + '\'' +
                ", Email='" + email + '\'' +
                ", picture='" + pic+ '\'' +
                ", Phone='" + Phone+ '\'' +
                '}';
    }
}
