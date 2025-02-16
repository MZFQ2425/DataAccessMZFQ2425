package com.mzfq.market.OnlineMarket.models.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.LocalDate;

public class FullProductDTO {
    @NotNull(message = "Product cannot be empty!")
    @Min(value = 1, message = "Product ID must be greater than 0")
    private Integer productId;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String productName;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative!")
    private double price;

    @FutureOrPresent(message = "Start date must be today or in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate offerStartDate;

    @FutureOrPresent(message = "End date must be today or in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate offerEndDate;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public LocalDate getOfferStartDate() {
        return offerStartDate;
    }

    public void setOfferStartDate(LocalDate offerStartDate) {
        this.offerStartDate = offerStartDate;
    }

    public LocalDate getOfferEndDate() {
        return offerEndDate;
    }

    public void setOfferEndDate(LocalDate offerEndDate) {
        this.offerEndDate = offerEndDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
