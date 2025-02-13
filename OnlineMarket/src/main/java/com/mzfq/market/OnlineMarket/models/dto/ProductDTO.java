package com.mzfq.market.OnlineMarket.models.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductDTO {
    @NotNull(message = "Product cannot be empty!")
    private int productId;
    @NotNull(message = "Category cannot be empty!")
    private int categoryId;
    @Min(value = 0, message = "Stock mustn't be lower than 0")
    private int stock;
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative!")
    private double price;

    public ProductDTO(int productId, String productName, String description, int categoryId, int stock, double price) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.stock = stock;
        this.price = price;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

