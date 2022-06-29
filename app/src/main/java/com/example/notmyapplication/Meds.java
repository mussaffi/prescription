package com.example.notmyapplication;

public class Meds {
    private String medName;
    private Integer amount;
    private Double price;
    private String expDate;
    private String picName;

    public Meds(String name, Double price, String expDate, String picName)
    {
        this.medName = name;
        this.price = price;
        this.expDate = expDate;
        this.picName= picName;
    }
    public Meds(String name, Double price, String expDate, Integer amount, String picName)
    {
        this.medName = name;
        this.price = price;
        this.expDate = expDate;
        this.amount=amount;
        this.picName = picName;
    }
    public Meds(String name, Double price, String picName)
    {
        this.medName = name;
        this.price = price;
        this.amount=null;
        this.expDate=null;
        this.picName = picName;
    }

    public Meds()
    {

    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public String getPicName() {
        return picName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }


    @Override
    public String toString()
    {
        return "com.example.databasework.Meds{" +
                "medName='" + medName + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", expDate='" + expDate + '\'' +
                '}';
    }
}
