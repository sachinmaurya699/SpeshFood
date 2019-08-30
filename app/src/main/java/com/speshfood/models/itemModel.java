package com.speshfood.models;

public class itemModel {
    public itemModel(){

    }
    private String itemName;
    private String qtyType;
    private String price;
    private String categoryName;

    public
    String getItemCode() {
        return itemCode;
    }

    public
    void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    private String itemCode;

    public
    String getCategoryCode() {
        return categoryCode;
    }

    public
    void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    private String categoryCode;

    public
    String getCategoryName() {
        return categoryName;
    }

    public
    void setCategoryName(String categoryname) {
        this.categoryName = categoryname;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQtyType() {
        return qtyType;
    }

    public void setQtyType(String qtyType) {
        this.qtyType = qtyType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
