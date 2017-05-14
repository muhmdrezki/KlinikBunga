package com.example.rezki.klinikbunga;

/**
 * Created by Rezki on 4/17/2017.
 */

public class Post1 {


    private String title;
    private String description;



    private String category;
    private String image;
    private String username;

    public Post1(){

    }

    public Post1(String title, String description, String image, String username, String category) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.username = username;
        this.category = category;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
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
