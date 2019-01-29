package com.example.mahmoudsadek.orderresturantserver.model;

/**
 * Created by Mahmoud Sadek on 11/29/2018.
 */

public class Banner {

    private String id, image, name;

    //alt+ insert


    public Banner() {
    }

    public Banner(String id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}