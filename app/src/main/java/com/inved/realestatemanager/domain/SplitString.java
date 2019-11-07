package com.inved.realestatemanager.domain;

public class SplitString {

    public String splitStringWithSpace(String stringToSplit, int column) {

        String[] splited = stringToSplit.split("\\s+");

        if (column == 0 || column == 1) {
            switch (column) {
                case 0:
                    return splited[0];

                case 1:
                    return splited[1];


            }

        }
        return null;
    }
}
