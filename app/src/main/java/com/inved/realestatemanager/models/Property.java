package com.inved.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(foreignKeys = @ForeignKey(entity = RealEstateAgents.class,parentColumns = "id",childColumns = "realEstateAgentId"),
        indices = {@Index("propertyId"), @Index(value = {"realEstateAgentId", "propertyId"})})

public class Property {

    @PrimaryKey(autoGenerate = true)
    private int propertyId;
    private String typeProperty;
    private double pricePropertyInDollar;
    private double surfaceAreaProperty;
    private int numberRoomsInProperty;
    private int numberBathroomsInProperty;
    private int numberBedroomsInProperty;
    private String fullDescriptionProperty;
    private String photoDescription;
    private String addressProperty;
    private String townProperty;
    private String statusProperty;
    private String dateOfEntryOnMarketForProperty;
    private String dateOfSaleForPorperty;
    private boolean selected;



    private int realEstateAgentId;

    public Property(){}

    public Property(String typeProperty, double pricePropertyInDollar,
                    double surfaceAreaProperty, int numberRoomsInProperty,
                    int numberBathroomsInProperty, int numberBedroomsInProperty,
                    String fullDescriptionProperty,
                    String photoDescription, String addressProperty,String townProperty,
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
        this.addressProperty = addressProperty;
        this.townProperty = townProperty;
        this.statusProperty = statusProperty;
        this.dateOfEntryOnMarketForProperty = dateOfEntryOnMarketForProperty;
        this.dateOfSaleForPorperty = dateOfSaleForPorperty;
        this.selected = selected;
        this.realEstateAgentId = realEstateAgentId;
    }


    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
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

    public void setPhoto(Photos photo) {
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public String getAddressProperty() {
        return addressProperty;
    }

    public void setAddressProperty(String addressProperty) {
        this.addressProperty = addressProperty;
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

    public int getRealEstateAgentId() {
        return realEstateAgentId;
    }

    public void setRealEstateAgentId(int realEstateAgentId) {
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
