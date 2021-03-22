package com.example.erenew;

import android.content.Intent;

import java.util.*;

import com.google.firebase.firestore.ServerTimestamp;


public class Vehicle  {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    private String type;
    private String IC;
    private String Name;
    private String Phone;
    private String PlateNo;
    private String ExpDate;
    private String Insurance;
    private String Email;
    private String Price;
    private String Quotation;




    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    private String Duration;

    public String getQuotation() {
        return Quotation;
    }

    public void setQuotation(String quotation) {
        Quotation = quotation;
    }


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }





    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    private String Status;

    public Date getDateRequest() {
        return DateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        DateRequest = dateRequest;
    }

    private @ServerTimestamp Date DateRequest;

    public Vehicle(String IC, String name, String phone, String plateNo, String expDate, String insurance, String Duration) {
        this.IC = IC;
        Name = name;
        this.Phone = phone;
        this.PlateNo = plateNo;
        this.ExpDate = expDate;
        this.Insurance = insurance;
        this.Duration = Duration;
    }

    public Vehicle()
    {

    }

    public String getIc() {
        return IC;
    }

    public void setIc(String IC) {
        this.IC = IC;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPlateNo() {
        return PlateNo;
    }

    public void setPlateNo(String PlateNo) {
        this.PlateNo = PlateNo;
    }

    public String getExpDate() {
        return ExpDate;
    }


    public String getInsurance() {
        return Insurance;
    }

    public void setInsurance(String insurance) {
        Insurance = insurance;
    }

    public void setExpDate(String expDate) {
        this.ExpDate = expDate;

    }


    public void startActivity(Intent intent) {
    }
}
