package com.inved.realestatemanager.domain;

import android.widget.CheckBox;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.utils.MainApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinCheckBoxInString {

    public String joinMethod(List<String> input) {

        if (input == null || input.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.size(); i++) {

            sb.append(input.get(i));

            // if not the last item
            if (i != input.size() - 1) {
                sb.append(",");
            }

        }

        return sb.toString();

    }

    public String fillPoiCheckboxList(CheckBox shopsCheckBox,CheckBox restaurantsCheckBox,CheckBox schoolCheckBox,CheckBox carParksCheckBox,CheckBox touristAttractionCheckBox,CheckBox oilStationCheckBox) {

        List<String> pointsOfInterestList = new ArrayList<>();
        //Delete all elements of the List before to verify is checkbox are checked.
        pointsOfInterestList.clear();

        // Check which checkbox was clicked
        if (shopsCheckBox.isChecked())
            pointsOfInterestList.add(MainApplication.getResourses().getString(R.string.fullscreen_dialog__poi_shops));
        if (restaurantsCheckBox.isChecked())
            pointsOfInterestList.add(MainApplication.getResourses().getString(R.string.fullscreen_dialog__poi_restaurants));
        if (schoolCheckBox.isChecked())
            pointsOfInterestList.add(MainApplication.getResourses().getString(R.string.fullscreen_dialog__poi_school));
        if (carParksCheckBox.isChecked())
            pointsOfInterestList.add(MainApplication.getResourses().getString(R.string.fullscreen_dialog_poi_car_parks));
        if (touristAttractionCheckBox.isChecked())
            pointsOfInterestList.add(MainApplication.getResourses().getString(R.string.fullscreen_dialog_poi_tourist_attraction));
        if (oilStationCheckBox.isChecked())
            pointsOfInterestList.add(MainApplication.getResourses().getString(R.string.fullscreen_dialog_poi_oil_station));

        String myList;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myList = String.join(",", pointsOfInterestList);
        }else{
            myList=joinMethod(pointsOfInterestList);
        }

        return myList;
    }

    //Split poi list from Room to know wich checkbox is checked
    public void splitPoiInCheckbox(String propertyPointOfInterest,CheckBox shopsCheckBox,CheckBox restaurantsCheckBox,CheckBox schoolCheckBox,CheckBox carParksCheckBox,CheckBox touristAttractionCheckBox,CheckBox oilStationCheckBox) {

        ArrayList<String> poiList = new ArrayList<>(Arrays.asList(propertyPointOfInterest.split(",")));

        if (poiList.contains(MainApplication.getResourses().getString(R.string.fullscreen_dialog__poi_school))) {

            schoolCheckBox.setChecked(true);
        }
        if (poiList.contains(MainApplication.getResourses().getString(R.string.fullscreen_dialog__poi_restaurants))) {
            restaurantsCheckBox.setChecked(true);
        }
        if (poiList.contains(MainApplication.getResourses().getString(R.string.fullscreen_dialog__poi_shops))) {
            shopsCheckBox.setChecked(true);
        }
        if (poiList.contains(MainApplication.getResourses().getString(R.string.fullscreen_dialog_poi_tourist_attraction))) {
            touristAttractionCheckBox.setChecked(true);
        }
        if (poiList.contains(MainApplication.getResourses().getString(R.string.fullscreen_dialog_poi_car_parks))) {
            carParksCheckBox.setChecked(true);
        }
        if (poiList.contains(MainApplication.getResourses().getString(R.string.fullscreen_dialog_poi_oil_station))) {
            oilStationCheckBox.setChecked(true);
        }

    }
}
