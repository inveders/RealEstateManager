package com.inved.realestatemanager.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RealEstateAgents {

    @PrimaryKey(autoGenerate = true)
    private long realEstateAgentId;

    private String firstname;
    private String lastname;
    private String urlPicture;
    private String agencyName;
    private String agencyPlaceId;
    private String email;

    public RealEstateAgents(){}

    public RealEstateAgents(String firstname, String lastname, String urlPicture,String agencyName,String agencyPlaceId,String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.urlPicture = urlPicture;
        this.agencyName =agencyName;
        this.agencyPlaceId=agencyPlaceId;
        this.email=email;
    }

    public long getRealEstateAgentId() {
        return realEstateAgentId;
    }

    public void setRealEstateAgentId(long realEstateAgentId) {
        this.realEstateAgentId = realEstateAgentId;
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

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyPlaceId() {
        return agencyPlaceId;
    }

    public void setAgencyPlaceId(String agencyPlaceId) {
        this.agencyPlaceId = agencyPlaceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "RealEstateAgents{" +
                "realEstateAgentId=" + realEstateAgentId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", urlPicture='" + urlPicture + '\'' +
                ", agencyName='" + agencyName + '\'' +
                ", agencyPlaceId='" + agencyPlaceId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
