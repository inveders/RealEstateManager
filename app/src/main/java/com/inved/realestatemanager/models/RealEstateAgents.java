package com.inved.realestatemanager.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RealEstateAgents {

    @NonNull
    @PrimaryKey
    private String realEstateAgentId;

    private String firstname;
    private String lastname;
    private String urlPicture;
    private String agencyName;
    private String agencyPlaceId;


    public RealEstateAgents(){}

    public RealEstateAgents(@NonNull String realEstateAgentId, String firstname, String lastname, String urlPicture, String agencyName, String agencyPlaceId) {
        this.realEstateAgentId=realEstateAgentId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.urlPicture = urlPicture;
        this.agencyName =agencyName;
        this.agencyPlaceId=agencyPlaceId;
    }

    public String getRealEstateAgentId() {
        return realEstateAgentId;
    }

    public void setRealEstateAgentId(@NonNull String realEstateAgentId) {
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


    @NonNull
    @Override
    public String toString() {
        return "RealEstateAgents{" +
                "realEstateAgentId=" + realEstateAgentId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", urlPicture='" + urlPicture + '\'' +
                ", agencyName='" + agencyName + '\'' +
                ", agencyPlaceId='" + agencyPlaceId + '\'' +
                '}';
    }
}
