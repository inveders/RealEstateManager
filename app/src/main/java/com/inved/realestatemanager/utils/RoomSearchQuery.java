package com.inved.realestatemanager.utils;

import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

public class RoomSearchQuery {


    public SimpleSQLiteQuery queryRoomDatabase(String type, String town, double minSurface, double maxSurface, double minPrice, double maxPrice,
                                               int minBedRoom, int maxBedRoom, String country, String status, String realEstateAgentId) {

        // RoomSearchQuery string
        String queryString = "";

        // List of bind parameters
        List<Object> args = new ArrayList<>();

        boolean containsCondition = false;

        // Beginning of query string
        queryString += "SELECT * FROM Property";

        // Optional parts are added to query string and to args upon here

        if (realEstateAgentId != null) {
            queryString += " WHERE";
            queryString += " realEstateAgentId = :realEstateAgentId";
            args.add(realEstateAgentId);
            containsCondition = true;
        }

        if (type != null) {

            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
                containsCondition = true;
            }

            queryString += " typeProperty LIKE :type";
            args.add(type);
        }

        if (town != null) {

            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
                containsCondition = true;
            }

            queryString += " townProperty LIKE :town";
            args.add(town);
        }


        if (minSurface>=0 && maxSurface <= 99999) {

            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
                containsCondition = true;
            }

            queryString += " surfaceAreaProperty BETWEEN :minSurface AND :maxSurface";
            args.add(minSurface);
            args.add(maxSurface);

        }

        if (minPrice>=0 && maxSurface <= 3000000) {

            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
                containsCondition = true;
            }

            queryString += " pricePropertyInEuro BETWEEN :minPrice AND :maxPrice";
            args.add(minPrice);
            args.add(maxPrice);
        }


        if (minBedRoom>=0) {
            if(maxBedRoom <7 && maxBedRoom!=0){
                if (containsCondition) {
                    queryString += " AND";
                } else {
                    queryString += " WHERE";
                    containsCondition = true;
                }

                queryString += " numberBedroomsInProperty BETWEEN :minBedRoom AND :maxBedRoom";
                args.add(minBedRoom);
                args.add(maxBedRoom);

            }else if(maxBedRoom==7){
                if (containsCondition) {
                    queryString += " AND";
                } else {
                    queryString += " WHERE";
                    containsCondition = true;
                }
                queryString += " numberBedroomsInProperty BETWEEN :minBedRoom AND 25";
                args.add(minBedRoom);

            }


        }

        if (country != null) {

            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
                containsCondition = true;
            }

            queryString += " country LIKE :country";
            args.add(country);
        }

        if (status != null) {

            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
            }

            queryString += " statusProperty LIKE :status";
            args.add(status);
        }

        return new SimpleSQLiteQuery(queryString, args.toArray());

    }


}
