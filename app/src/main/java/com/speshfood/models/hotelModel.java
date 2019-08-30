package com.speshfood.models;

public class hotelModel {
    private String hotelName;
    private String category;
    private String rating;

    public
    String getHotelCode() {
        return hotelCode;
    }

    public
    void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    private String hotelCode;
    public
    hotelModel(){
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getHotelName() {
        return hotelName;
    }
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

}
