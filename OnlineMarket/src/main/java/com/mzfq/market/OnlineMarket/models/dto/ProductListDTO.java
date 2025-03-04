package com.mzfq.market.OnlineMarket.models.dto;

import javax.validation.constraints.*;

public class ProductListDTO {
    @NotNull(message = "Product cannot be empty!")
    private int productId;
    @NotNull(message = "Category cannot be empty!")
    private int categoryId;
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative!")
    private double price;
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative!")
    private double offerPrice;
    @NotNull(message = "Product name cannot be empty!")
    private String productName;
    @NotNull(message = "Category name cannot be empty!")
    private String categoryName;

    public ProductListDTO(){

    }

    public ProductListDTO(int productId, int categoryId, double price, double offerPrice, String productName, String categoryName) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.offerPrice = offerPrice;
        this.price = price;
        this.productName = productName;
        this.categoryName = categoryName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

