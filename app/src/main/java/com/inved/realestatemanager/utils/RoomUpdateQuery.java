package com.inved.realestatemanager.utils;

import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

public class  RoomUpdateQuery {

    public SimpleSQLiteQuery queryRoomUpdateDatabase(String typeProperty, double pricePropertyInEuro,
                                                     double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                                                     int numberBedroomsInProperty, String fullDescriptionText, String streetNumber,
                                                     String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                                                     String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForPorperty,
                                                     boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                                                     String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                                                     String photoDescription4, String photoDescription5, String realEstateAgentId,String propertyId) {

        // RoomSearchQuery string
        String queryString = "";

        // List of bind parameters
        List<Object> args = new ArrayList<>();

        // Beginning of query string
        queryString += "UPDATE Property SET";

        if (typeProperty != null) {

            queryString += " typeProperty = :typeProperty";
            queryString += ",";
            args.add(typeProperty);

        }

        if (pricePropertyInEuro != 0.0) {

            queryString += " pricePropertyInEuro = :pricePropertyInEuro";
            queryString += ",";
            args.add(pricePropertyInEuro);
        }

        if (surfaceAreaProperty != 0.0) {

            queryString += " surfaceAreaProperty = :surfaceAreaProperty";
            queryString += ",";
            args.add(surfaceAreaProperty);
        }

        if (numberRoomsInProperty != null) {

            queryString += " numberRoomsInProperty = :numberRoomsInProperty";
            queryString += ",";
            args.add(numberRoomsInProperty);
        }

        if (numberBathroomsInProperty != null) {

            queryString += " numberBathroomsInProperty = :numberBathroomsInProperty";
            queryString += ",";
            args.add(numberBathroomsInProperty);
        }

        if (numberBedroomsInProperty != -1) {

            queryString += " numberBedroomsInProperty = :numberBedroomsInProperty";
            queryString += ",";
            args.add(numberBedroomsInProperty);
        }

        if (fullDescriptionText != null) {

            queryString += " fullDescriptionProperty = :fullDescriptionText";
            queryString += ",";
            args.add(fullDescriptionText);
        }

        if (streetNumber != null) {

            queryString += " streetNumber = :streetNumber";
            queryString += ",";
            args.add(streetNumber);
        }



        if (streetName != null) {

            queryString += " streetName = :streetName";
            queryString += ",";
            args.add(streetName);
        }

        if (zipCode != null) {

            queryString += " zipCode = :zipCode";
            queryString += ",";
            args.add(zipCode);
        }

        if (townProperty != null) {

            queryString += " townProperty = :townProperty";
            queryString += ",";
            args.add(townProperty);
        }

        if (country != null) {

            queryString += " country = :country";
            queryString += ",";
            args.add(country);
        }


        if (addressCompl != null) {

            queryString += " addressCompl = :addressCompl";
            queryString += ",";
            args.add(addressCompl);
        }

        if (pointOfInterest != null) {


            queryString += " pointOfInterest = :pointOfInterest";
            queryString += ",";
            args.add(pointOfInterest);
        }

        if (statusProperty != null) {


            queryString += " statusProperty = :statusProperty";
            queryString += ",";
            args.add(statusProperty);
        }

        if (dateOfEntryOnMarketForProperty != null) {


            queryString += " dateOfEntryOnMarketForProperty = :dateOfEntryOnMarketForProperty";
            queryString += ",";
            args.add(dateOfEntryOnMarketForProperty);
        }

        if (dateOfSaleForPorperty != null) {

            queryString += " dateOfSaleForProperty = :dateOfSaleForProperty";
            queryString += ",";
            args.add(dateOfSaleForPorperty);
        }


        if (selected) {

            queryString += " selected = :selected";
            queryString += ",";
            args.add(true);
        }

        if (photoUri1 != null) {

            queryString += " photoUri1 = :photoUri1";
            queryString += ",";
            args.add(photoUri1);
        }

        if (photoUri2 != null) {


            queryString += " photoUri2 = :photoUri2";
            queryString += ",";
            args.add(photoUri2);
        }

        if (photoUri3 != null) {


            queryString += " photoUri3 = :photoUri3";
            queryString += ",";
            args.add(photoUri3);
        }

        if (photoUri4 != null) {

            queryString += " photoUri4 = :photoUri4";
            queryString += ",";
            args.add(photoUri4);
        }

        if (photoUri5 != null) {


            queryString += " photoUri5 = :photoUri5";
            queryString += ",";
            args.add(photoUri5);
        }

        if (photoDescription1 != null) {

            queryString += " photoDescription1 = :photoDescription1";
            queryString += ",";
            args.add(photoDescription1);
        }

        if (photoDescription2 != null) {

            queryString += " photoDescription2 = :photoDescription2";
            queryString += ",";
            args.add(photoDescription2);
        }

        if (photoDescription3 != null) {


            queryString += " photoDescription3 = :photoDescription3";
            queryString += ",";
            args.add(photoDescription3);
        }

        if (photoDescription4 != null) {

            queryString += " photoDescription4 = :photoDescription4";
            queryString += ",";
            args.add(photoDescription4);
        }


        if (photoDescription5 != null) {

            queryString += " photoDescription5 = :photoDescription5";
            queryString += ",";
            args.add(photoDescription5);
        }


        if (realEstateAgentId != null) {

            queryString += " realEstateAgentId = :realEstateAgentId";
            args.add(realEstateAgentId);
        }


        if (propertyId != null) {

            queryString += " WHERE propertyId = :propertyId";
            args.add(propertyId);
        }

        return new SimpleSQLiteQuery(queryString, args.toArray());

    }
}
