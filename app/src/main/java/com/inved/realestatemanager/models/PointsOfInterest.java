package com.inved.realestatemanager.models;

import java.util.List;

public class PointsOfInterest {

    private List<String> pointsOfInterest;

    public PointsOfInterest(List<String> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    public List<String> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void setPointsOfInterest(List<String> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }
}
