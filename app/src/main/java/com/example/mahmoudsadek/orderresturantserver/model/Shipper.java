package com.example.mahmoudsadek.orderresturantserver.model;

/**
 * Created by Mahmoud Sadek on 11/29/2018.
 */

public class Shipper {

    private String name, phone, password;

    //alt+ insert


    public Shipper() {
    }

    public Shipper(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}