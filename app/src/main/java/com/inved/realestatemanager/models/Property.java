package com.inved.realestatemanager.models;

import android.content.ContentValues;

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
    private double pricePropertyInEuro;
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
    private String dateOfSaleForProperty;
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

    public Property(@NonNull String propertyId, String typeProperty, double pricePropertyInEuro,
                    double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                    int numberBedroomsInProperty, String fullDescriptionProperty, String streetNumber,
                    String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                    String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForProperty,
                    boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                    String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                    String photoDescription4, String photoDescription5, String realEstateAgentId) {

        this.propertyId=propertyId;
        this.typeProperty = typeProperty;
        this.pricePropertyInEuro = pricePropertyInEuro;
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
        this.dateOfSaleForProperty = dateOfSaleForProperty;
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

    // --- UTILS ---
    public static Property fromContentValues(ContentValues values) {
        final Property property = new Property("",null,0.0,0.0,null,null,0,null,null,null,null,null,null,null,null,null,null,
                null,false,null,null,null,null,null,null,null,null,null,null,"");
        if (values.containsKey("propertyId")) property.setPropertyId(values.getAsString("propertyId"));
        if (values.containsKey("typeProperty")) property.setTypeProperty(values.getAsString("typeProperty"));
        if (values.containsKey("pricePropertyInEuro")) property.setPricePropertyInEuro(values.getAsDouble("pricePropertyInEuro"));
        if (values.containsKey("surfaceAreaProperty")) property.setSurfaceAreaProperty(values.getAsDouble("surfaceAreaProperty"));
        if (values.containsKey("numberRoomsInProperty")) property.setNumberRoomsInProperty(values.getAsString("numberRoomsInProperty"));
        if (values.containsKey("numberBathroomsInProperty")) property.setNumberBathroomsInProperty(values.getAsString("numberBathroomsInProperty"));
        if (values.containsKey("numberBedroomsInProperty")) property.setNumberBedroomsInProperty(values.getAsInteger("numberBedroomsInProperty"));
        if (values.containsKey("fullDescriptionProperty")) property.setFullDescriptionProperty(values.getAsString("fullDescriptionProperty"));

        if (values.containsKey("streetNumber")) property.setStreetNumber(values.getAsString("streetNumber"));
        if (values.containsKey("streetName")) property.setStreetName(values.getAsString("streetName"));
        if (values.containsKey("zipCode")) property.setZipCode(values.getAsString("zipCode"));
        if (values.containsKey("townProperty")) property.setTownProperty(values.getAsString("townProperty"));
        if (values.containsKey("country")) property.setCountry(values.getAsString("country"));
        if (values.containsKey("addressCompl")) property.setAddressCompl(values.getAsString("addressCompl"));
        if (values.containsKey("pointOfInterest")) property.setPointOfInterest(values.getAsString("pointOfInterest"));
        if (values.containsKey("statusProperty")) property.setStatusProperty(values.getAsString("statusProperty"));
        if (values.containsKey("dateOfEntryOnMarketForProperty")) property.setDateOfEntryOnMarketForProperty(values.getAsString("dateOfEntryOnMarketForProperty"));
        if (values.containsKey("dateOfSaleForProperty")) property.setDateOfSaleForProperty(values.getAsString("dateOfSaleForProperty"));

        if (values.containsKey("selected")) property.setSelected(values.getAsBoolean("selected"));
        if (values.containsKey("photoUri1")) property.setPhotoUri1(values.getAsString("photoUri1"));
        if (values.containsKey("photoUri2")) property.setPhotoUri2(values.getAsString("photoUri2"));
        if (values.containsKey("photoUri3")) property.setPhotoUri3(values.getAsString("photoUri3"));
        if (values.containsKey("photoUri4")) property.setPhotoUri4(values.getAsString("photoUri4"));
        if (values.containsKey("photoUri5")) property.setPhotoUri5(values.getAsString("photoUri5"));
        if (values.containsKey("photoDescription1")) property.setPhotoDescription1(values.getAsString("photoDescription1"));
        if (values.containsKey("photoDescription2")) property.setPhotoDescription2(values.getAsString("photoDescription2"));
        if (values.containsKey("photoDescription3")) property.setPhotoDescription3(values.getAsString("photoDescription3"));
        if (values.containsKey("photoDescription4")) property.setPhotoDescription4(values.getAsString("photoDescription4"));
        if (values.containsKey("photoDescription5")) property.setPhotoDescription5(values.getAsString("photoDescription5"));
        if (values.containsKey("realEstateAgentId")) property.setRealEstateAgentId(values.getAsString("realEstateAgentId"));

        return property;
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

    public double getPricePropertyInEuro() {
        return pricePropertyInEuro;
    }

    public void setPricePropertyInEuro(double pricePropertyInEuro) {
        this.pricePropertyInEuro = pricePropertyInEuro;
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

    public String getDateOfSaleForProperty() {
        return dateOfSaleForProperty;
    }

    public void setDateOfSaleForProperty(String dateOfSaleForProperty) {
        this.dateOfSaleForProperty = dateOfSaleForProperty;
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
                ", pricePropertyInEuro=" + pricePropertyInEuro +
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
                ", dateOfSaleForProperty='" + dateOfSaleForProperty + '\'' +
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
