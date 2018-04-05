package com.example.jafethvasquez.labmoviles_jafethvsquez;

import android.media.Image;

public class Person {
    private String name = "";
    private String Profession = "";
    private String picture = "";
    private String gender ="";


    public Person(String name, String profession, String picture, String gender) {
        this.name = name;
        Profession = profession;
        this.picture = picture;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

