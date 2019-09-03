package com.inved.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RealEstateAgents {


    @PrimaryKey(autoGenerate = true)
    private long id;
    private String firstname;
    private String lastname;
    private String urlPicture;

    public RealEstateAgents(){}

    public RealEstateAgents(long id, String firstname, String lastname, String urlPicture) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.urlPicture = urlPicture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
