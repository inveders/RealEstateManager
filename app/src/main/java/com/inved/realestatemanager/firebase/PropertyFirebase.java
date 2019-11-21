package com.inved.realestatemanager.firebase;


public class PropertyFirebase {


        private long propertyId;
        private String typeProperty;
        private double pricePropertyInDollar;
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
        private String dateOfSaleForPorperty;
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
        private long realEstateAgentId;

        public PropertyFirebase(){}

        public PropertyFirebase(long propertyId, String typeProperty, double pricePropertyInDollar,
                        double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                        int numberBedroomsInProperty, String fullDescriptionProperty, String streetNumber,
                        String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                        String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForPorperty,
                        boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                        String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                        String photoDescription4, String photoDescription5, long realEstateAgentId) {

            this.propertyId=propertyId;
            this.typeProperty = typeProperty;
            this.pricePropertyInDollar = pricePropertyInDollar;
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
            this.dateOfSaleForPorperty = dateOfSaleForPorperty;
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


}
