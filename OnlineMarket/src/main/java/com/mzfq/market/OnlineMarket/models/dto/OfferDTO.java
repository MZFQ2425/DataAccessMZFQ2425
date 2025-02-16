package com.mzfq.market.OnlineMarket.models.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class OfferDTO {

    @NotNull(message = "Product cannot be empty!")
    @Min(value = 1, message = "Please, select a product")
    private Integer productId;

    @NotNull(message = "Start date cannot be empty!")
    @FutureOrPresent(message = "Start date must be today or in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate offerStartDate;

    @NotNull(message = "End date cannot be empty!")
    @FutureOrPresent(message = "End date must be today or in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate offerEndDate;

    @NotNull(message = "Discount cannot be empty!")
    @Min(value = 0, message = "Discount must be at least 0")
    @Max(value = 50, message = "Discount cannot exceed 100%")
    private Integer discount;

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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}