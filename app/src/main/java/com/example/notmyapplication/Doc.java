package com.example.notmyapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Doc implements Serializable {
    private String docName;
    private String docPass;
    private String docEmail;
    private String docKey;
    private String docPhone;
    private ArrayList<String> userList;

    public Doc(String name, String pass, String email,String docPhone, String key) {
        this.docName = name;
        this.docPass = pass;
        this.docEmail = email;
        this.docPhone=docPhone;
        this.docKey = key;
        this.userList = new ArrayList<String>();
    }
    public Doc(String name, String pass, String email,String docPhone, String key,ArrayList<String> userList ) {
        this.docName = name;
        this.docPass = pass;
        this.docEmail = email;
        this.docPhone=docPhone;
        this.docKey = key;
        this.userList = userList;
    }

    public Doc() {
        this.docName = null;
        this.docPass = null;
        this.docEmail = null;
        this.docPhone = null;
        this.docKey = null;
    }



    public void AddUserKeyToDoc(String key){
        userList.add(key);
    }

    public String getDocName() {
        return docName;
    }

    public String getDocPassWord() {
        return docPass;
    }

    public String getDocEmail() {
        return this.docEmail;
    }

    public String getDocKey() {
        return this.docKey;
    }

    public ArrayList getUserList() {
        return userList;
    }

    public String getDocPhone() {
        return docPhone;
    }

    public void setDocName(String userName) {
        this.docName = userName;
    }

    public void setDocPassWord(String passWord) {
        this.docPass = passWord;
    }

    public void setDocEmail(String email) {
        this.docEmail = email;
    }

    public void setDocKey(String key) {
        this.docKey = key;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public void setDocPhone(String docPhone) {
        this.docPhone = docPhone;
    }

    @Override
    public String toString() {
        return "com.example.databasework.user{" +
                "UserName='" + docName + '\'' +
                ", PassWord='" + docPass + '\'' +
                ", Email='" + docEmail + '\'' +
                ", Email='" + userList + '\'' +
                ", docPhone='" + docPhone+ '\'' +
                '}';

    }
}