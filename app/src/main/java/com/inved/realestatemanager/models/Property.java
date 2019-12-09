package com.inved.realestatemanager.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = RealEstateAgents.class,parentColumns = "realEstateAgentId",childColumns = "realEstateAgentId"),
        indices = {@Index("propertyId"), @Index(value = {"realEstateAgentId", "propertyId"})})

public class Property {

    @NonNull
    @PrimaryKey
    private String propertyId;
    private String typeProperty;
    private double pricePropertyInDollar;
    private double surfaceAreaProperty;
    private String numberRoomsInProperty;
    private String numberBathroomsInProperty;
    private int numberBedroomsInProperty;
    private String fullDescriptionProperty;

    private String streetNumber;
    private String streetName;
    private String zipCode;
    private String townProperty;
    private String country;
    private String addressCompl;
    private String pointOfInterest;
    private String statusProperty;
    private String dateOfEntryOnMarketForProperty;
    private String dateOfSaleForPorperty;
    private boolean selected;
    private String photoUri1;
    private String photoUri2;
    private String photoUri3;
    private String photoUri4;
    private String photoUri5;
    private String photoDescription1;
    private String photoDescription2;
    private String photoDescription3;
    private String photoDescription4;
    private String photoDescription5;
    private String realEstateAgentId;

    public Property(){}

    public Property(@NonNull String propertyId, String typeProperty, double pricePropertyInDollar,
                    double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                    int numberBedroomsInProperty, String fullDescriptionProperty, String streetNumber,
                    String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                    String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForPorperty,
                    boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                    String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                    String photoDescription4, String photoDescription5, String realEstateAgentId) {

        this.propertyId=propertyId;
        this.typeProperty = typeProperty;
        this.pricePropertyInDollar = pricePropertyInDollar;
        this.surfaceAreaProperty = surfaceAreaProperty;
        this.numberRoomsInProperty = numberRoomsInProperty;
        this.numberBathroomsInProperty = numberBathroomsInProperty;
        this.numberBedroomsInProperty = numberBedroomsInProperty;
        this.fullDescriptionProperty = fullDescriptionProperty;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.townProperty = townProperty;
        this.country = country;
        this.addressCompl = addressCompl;
        this.pointOfInterest = pointOfInterest;
        this.statusProperty = statusProperty;
        this.dateOfEntryOnMarketForProperty = dateOfEntryOnMarketForProperty;
        this.dateOfSaleForPorperty = dateOfSaleForPorperty;
        this.selected = selected;
        this.photoUri1 = photoUri1;
        this.photoUri2 = photoUri2;
        this.photoUri3 = photoUri3;
        this.photoUri4 = photoUri4;
        this.photoUri5 = photoUri5;
        this.photoDescription1 = photoDescription1;
        this.photoDescription2 = photoDescription2;
        this.photoDescription3 = photoDescription3;
        this.photoDescription4 = photoDescription4;
        this.photoDescription5 = photoDescription5;
        this.realEstateAgentId = realEstateAgentId;
    }

    public void setPropertyId(@NonNull String propertyId) {
        this.propertyId = propertyId;
    }

    @NonNull
    public String getPropertyId() {
        return propertyId;
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(String typeProperty) {
        this.typeProperty = typeProperty;
    }

    public double getPricePropertyInDollar() {
        return pricePropertyInDollar;
    }

    public void setPricePropertyInDollar(double pricePropertyInDollar) {
        this.pricePropertyInDollar = pricePropertyInDollar;
    }

    public double getSurfaceAreaProperty() {
        return surfaceAreaProperty;
    }

    public void setSurfaceAreaProperty(double surfaceAreaProperty) {
        this.surfaceAreaProperty = surfaceAreaProperty;
    }

    public String getNumberRoomsInProperty() {
        return numberRoomsInProperty;
    }

    public void setNumberRoomsInProperty(String numberRoomsInProperty) {
        this.numberRoomsInProperty = numberRoomsInProperty;
    }

    public String getNumberBathroomsInProperty() {
        return numberBathroomsInProperty;
    }

    public void setNumberBathroomsInProperty(String numberBathroomsInProperty) {
        this.numberBathroomsInProperty = numberBathroomsInProperty;
    }

    public int getNumberBedroomsInProperty() {
        return numberBedroomsInProperty;
    }

    public void setNumberBedroomsInProperty(int numberBedroomsInProperty) {
        this.numberBedroomsInProperty = numberBedroomsInProperty;
    }

    public String getFullDescriptionProperty() {
        return fullDescriptionProperty;
    }

    public void setFullDescriptionProperty(String fullDescriptionProperty) {
        this.fullDescriptionProperty = fullDescriptionProperty;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTownProperty() {
        return townProperty;
    }

    public void setTownProperty(String townProperty) {
        this.townProperty = townProperty;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressCompl() {
        return addressCompl;
    }

    public void setAddressCompl(String addressCompl) {
        this.addressCompl = addressCompl;
    }

    public String getPointOfInterest() {
        return pointOfInterest;
    }

    public void setPointOfInterest(String pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
    }

    public String getStatusProperty() {
        return statusProperty;
    }

    public void setStatusProperty(String statusProperty) {
        this.statusProperty = statusProperty;
    }

    public String getDateOfEntryOnMarketForProperty() {
        return dateOfEntryOnMarketForProperty;
    }

    public void setDateOfEntryOnMarketForProperty(String dateOfEntryOnMarketForProperty) {
        this.dateOfEntryOnMarketForProperty = dateOfEntryOnMarketForProperty;
    }

    public String getDateOfSaleForPorperty() {
        return dateOfSaleForPorperty;
    }

    public void setDateOfSaleForPorperty(String dateOfSaleForPorperty) {
        this.dateOfSaleForPorperty = dateOfSaleForPorperty;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPhotoUri1() {
        return photoUri1;
    }

    public void setPhotoUri1(String photoUri1) {
        this.photoUri1 = photoUri1;
    }

    public String getPhotoUri2() {
        return photoUri2;
    }

    public void setPhotoUri2(String photoUri2) {
        this.photoUri2 = photoUri2;
    }

    public String getPhotoUri3() {
        return photoUri3;
    }

    public void setPhotoUri3(String photoUri3) {
        this.photoUri3 = photoUri3;
    }

    public String getPhotoUri4() {
        return photoUri4;
    }

    public void setPhotoUri4(String photoUri4) {
        this.photoUri4 = photoUri4;
    }

    public String getPhotoUri5() {
        return photoUri5;
    }

    public void setPhotoUri5(String photoUri5) {
        this.photoUri5 = photoUri5;
    }

    public String getPhotoDescription1() {
        return photoDescription1;
    }

    public void setPhotoDescription1(String photoDescription1) {
        this.photoDescription1 = photoDescription1;
    }

    public String getPhotoDescription2() {
        return photoDescription2;
    }

    public void setPhotoDescription2(String photoDescription2) {
        this.photoDescription2 = photoDescription2;
    }

    public String getPhotoDescription3() {
        return photoDescription3;
    }

    public void setPhotoDescription3(String photoDescription3) {
        this.photoDescription3 = photoDescription3;
    }

    public String getPhotoDescription4() {
        return photoDescription4;
    }

    public void setPhotoDescription4(String photoDescription4) {
        this.photoDescription4 = photoDescription4;
    }

    public String getPhotoDescription5() {
        return photoDescription5;
    }

    public void setPhotoDescription5(String photoDescription5) {
        this.photoDescription5 = photoDescription5;
    }

    public String getRealEstateAgentId() {
        return realEstateAgentId;
    }

    public void setRealEstateAgentId(String realEstateAgentId) {
        this.realEstateAgentId = realEstateAgentId;
    }


    @NonNull
    @Override
    public String toString() {
        return "Property{" +
                "propertyId=" + propertyId +
                ", typeProperty='" + typeProperty + '\'' +
                ", pricePropertyInDollar=" + pricePropertyInDollar +
                ", surfaceAreaProperty=" + surfaceAreaProperty +
                ", numberRoomsInProperty='" + numberRoomsInProperty + '\'' +
                ", numberBathroomsInProperty='" + numberBathroomsInProperty + '\'' +
                ", numberBedroomsInProperty=" + numberBedroomsInProperty +
                ", fullDescriptionProperty='" + fullDescriptionProperty + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", townProperty='" + townProperty + '\'' +
                ", country='" + country + '\'' +
                ", addressCompl='" + addressCompl + '\'' +
                ", pointOfInterest='" + pointOfInterest + '\'' +
                ", statusProperty='" + statusProperty + '\'' +
                ", dateOfEntryOnMarketForProperty='" + dateOfEntryOnMarketForProperty + '\'' +
                ", dateOfSaleForPorperty='" + dateOfSaleForPorperty + '\'' +
                ", selected=" + selected +
                ", photoUri1='" + photoUri1 + '\'' +
                ", photoUri2='" + photoUri2 + '\'' +
                ", photoUri3='" + photoUri3 + '\'' +
                ", photoUri4='" + photoUri4 + '\'' +
                ", photoUri5='" + photoUri5 + '\'' +
                ", photoDescription1='" + photoDescription1 + '\'' +
                ", photoDescription2='" + photoDescription2 + '\'' +
                ", photoDescription3='" + photoDescription3 + '\'' +
                ", photoDescription4='" + photoDescription4 + '\'' +
                ", photoDescription5='" + photoDescription5 + '\'' +
                ", realEstateAgentId=" + realEstateAgentId +
                '}';
    }
}
