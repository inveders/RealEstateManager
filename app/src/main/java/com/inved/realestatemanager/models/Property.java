package com.inved.realestatemanager.models;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = RealEstateAgents.class,parentColumns = "id",childColumns = "realEstateAgendId"))
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
    private List<Bitmap> photo;
    private String photoDescription;
    private String addressProperty;
    private List<String> pointsOfInterestNearProperty;
    private String statusProperty;
    private String dateOfEntryOnMarketForProperty;
    private String dateOfSaleForPorperty;
    private boolean selected;



    private long realEstateAgentId;

    public Property(){}

    public Property(String typeProperty, double pricePropertyInDollar,
                    double surfaceAreaProperty, int numberRoomsInProperty,
                    int numberBathroomsInProperty, int numberBedroomsInProperty,
                    String fullDescriptionProperty, List<Bitmap> photo,
                    String photoDescription, String addressProperty,
                    List<String> pointsOfInterestNearProperty,
                    String statusProperty, String dateOfEntryOnMarketForProperty,
                    String dateOfSaleForPorperty, boolean selected, long realEstateAgentId) {
        this.typeProperty = typeProperty;
        this.pricePropertyInDollar = pricePropertyInDollar;
        this.surfaceAreaProperty = surfaceAreaProperty;
        this.numberRoomsInProperty = numberRoomsInProperty;
        this.numberBathroomsInProperty = numberBathroomsInProperty;
        this.numberBedroomsInProperty = numberBedroomsInProperty;
        this.fullDescriptionProperty = fullDescriptionProperty;
        this.photo = photo;
        this.photoDescription = photoDescription;
        this.addressProperty = addressProperty;
        this.pointsOfInterestNearProperty = pointsOfInterestNearProperty;
        this.statusProperty = statusProperty;
        this.dateOfEntryOnMarketForProperty = dateOfEntryOnMarketForProperty;
        this.dateOfSaleForPorperty = dateOfSaleForPorperty;
        this.selected = selected;
        this.realEstateAgentId = realEstateAgentId;
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

    public void setFullDescriptionProperty(String fullDescriptionProperty) {
        this.fullDescriptionProperty = fullDescriptionProperty;
    }

    public List<Bitmap> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Bitmap> photo) {
        this.photo = photo;
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

    public List<String> getPointsOfInterestNearProperty() {
        return pointsOfInterestNearProperty;
    }

    public void setPointsOfInterestNearProperty(List<String> pointsOfInterestNearProperty) {
        this.pointsOfInterestNearProperty = pointsOfInterestNearProperty;
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
}
