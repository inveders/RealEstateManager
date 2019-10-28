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
    private int numberRoomsInProperty;
    private int numberBathroomsInProperty;
    private int numberBedroomsInProperty;
    private String fullDescriptionProperty;
    private String photoDescription;
    private String streeNumber;
    private String streetName;
    private String zipCode;
    private String townProperty;
    private String country;
    private String statusProperty;
    private String dateOfEntryOnMarketForProperty;
    private String dateOfSaleForPorperty;
    private boolean selected;

    private long realEstateAgentId;

    public Property(){}

    public Property(String typeProperty, double pricePropertyInDollar,
                    double surfaceAreaProperty, int numberRoomsInProperty,
                    int numberBathroomsInProperty, int numberBedroomsInProperty,
                    String fullDescriptionProperty,
                    String photoDescription,String streetNumber,String streetName, String zipCode, String townProperty, String country,
                    String statusProperty, String dateOfEntryOnMarketForProperty,
                    String dateOfSaleForPorperty, boolean selected, int realEstateAgentId) {
        this.typeProperty = typeProperty;
        this.pricePropertyInDollar = pricePropertyInDollar;
        this.surfaceAreaProperty = surfaceAreaProperty;
        this.numberRoomsInProperty = numberRoomsInProperty;
        this.numberBathroomsInProperty = numberBathroomsInProperty;
        this.numberBedroomsInProperty = numberBedroomsInProperty;
        this.fullDescriptionProperty = fullDescriptionProperty;
        this.photoDescription = photoDescription;
        this.streeNumber = streetNumber;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.townProperty = townProperty;
        this.country = country;
        this.statusProperty = statusProperty;
        this.dateOfEntryOnMarketForProperty = dateOfEntryOnMarketForProperty;
        this.dateOfSaleForPorperty = dateOfSaleForPorperty;
        this.selected = selected;
        this.realEstateAgentId = realEstateAgentId;
    }


    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public String getStreeNumber() {
        return streeNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
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

    public int getNumberRoomsInProperty() {
        return numberRoomsInProperty;
    }

    public void setNumberRoomsInProperty(int numberRoomsInProperty) {
        this.numberRoomsInProperty = numberRoomsInProperty;
    }

    public int getNumberBathroomsInProperty() {
        return numberBathroomsInProperty;
    }

    public void setNumberBathroomsInProperty(int numberBathroomsInProperty) {
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

    public String getTownProperty() {
        return townProperty;
    }

    public void setFullDescriptionProperty(String fullDescriptionProperty) {
        this.fullDescriptionProperty = fullDescriptionProperty;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public void setStreeNumber(String streeNumber) {
        this.streeNumber = streeNumber;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public long getRealEstateAgentId() {
        return realEstateAgentId;
    }

    public void setRealEstateAgentId(long realEstateAgentId) {
        this.realEstateAgentId = realEstateAgentId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setTownProperty(String townProperty) {
        this.townProperty = townProperty;
    }
}
