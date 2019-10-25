package com.inved.realestatemanager.models;

import androidx.room.TypeConverter;

import java.util.List;

public class PointsOfInterest {

    private List<String> pointsOfInterest;

    public PointsOfInterest(List<String> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    @TypeConverter
    public List<String> getPointsOfInterest() {
        return pointsOfInterest;
    }

    @TypeConverter
    public void setPointsOfInterest(List<String> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }
}
