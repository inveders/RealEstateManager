package com.inved.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = RealEstateAgents.class,parentColumns = "id",childColumns = "realEstateAgentId"),
        indices = {@Index("propertyId"), @Index(value = {"realEstateAgentId", "propertyId"})})

public class Property {

    @PrimaryKey(autoGenerate = true)
    private long propertyId;
    private String typeProperty;
    private double pricePropertyInDollar;
    private double surfaceAreaProperty;
    private String numberRoomsInProperty;
    private String numberBathroomsInProperty;
    private String numberBedroomsInProperty;
    private String fullDescriptionProperty;

    private String streetNumber;
    private String streetName;
    private String zipCode;
    private String townProperty;
    private String country;
    private String addressProperty;
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
    private long realEstateAgentId;

    public Property(){}

    public Property(String typeProperty, double pricePropertyInDollar,
                    double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                    String numberBedroomsInProperty, String fullDescriptionProperty, String streeNumber,
                    String streetName, String zipCode, String townProperty, String country,String addressProperty, String pointOfInterest,
                    String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForPorperty,
                    boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                    String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                    String photoDescription4, String photoDescription5, long realEstateAgentId) {

        this.typeProperty = typeProperty;
        this.pricePropertyInDollar = pricePropertyInDollar;
        this.surfaceAreaProperty = surfaceAreaProperty;
        this.numberRoomsInProperty = numberRoomsInProperty;
        this.numberBathroomsInProperty = numberBathroomsInProperty;
        this.numberBedroomsInProperty = numberBedroomsInProperty;
        this.fullDescriptionProperty = fullDescriptionProperty;
        this.streetNumber = streeNumber;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.townProperty = townProperty;
        this.country = country;
        this.addressProperty = addressProperty;
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

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public long getPropertyId() {
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

    public String getNumberBedroomsInProperty() {
        return numberBedroomsInProperty;
    }

    public void setNumberBedroomsInProperty(String numberBedroomsInProperty) {
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

    public String getAddressProperty() {
        return addressProperty;
    }

    public void setAddressProperty(String addressProperty) {
        this.addressProperty = addressProperty;
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

    public long getRealEstateAgentId() {
        return realEstateAgentId;
    }

    public void setRealEstateAgentId(long realEstateAgentId) {
        this.realEstateAgentId = realEstateAgentId;
    }
}
