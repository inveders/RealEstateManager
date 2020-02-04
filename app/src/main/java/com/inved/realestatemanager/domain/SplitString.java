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


    public String replaceAllSpacesByAddition(String stringToConvert){
        return stringToConvert.replaceAll("\\s", "+");
    }

    public String lastCharacters(String chaine, int numberCharacters)
    {
        if (chaine.length() <= numberCharacters)
            return(chaine);
        else
            return(chaine.substring(chaine.length() - numberCharacters));
    }
}
