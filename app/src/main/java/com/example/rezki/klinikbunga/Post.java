package com.example.rezki.klinikbunga;

/**
 * Created by Rezki on 4/17/2017.
 */

public class Post {


    private String title, description, image, username;

    public Post(){

    }

    public Post(String title, String description, String image, String username) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }





}
