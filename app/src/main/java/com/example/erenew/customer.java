package com.example.erenew;

public class customer {
    private String name;
    private String nric;
    private String plate;
    private String phone;

    public customer()
    {

    }

    public customer(String name, String nric, String plate, String phone) {
        this.name = name;
        this.nric = nric;
        this.plate = plate;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public String getPlateNum() {
        return plate;
    }

    public String getPhone() {
        return phone;
    }

    public void setCustname(String name) {
        this.name = name;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public void setPlateNum(String plate) {
        this.plate = plate;
    }

    public void setPhoneNum(String phone) {
        this.phone = phone;
    }
}
