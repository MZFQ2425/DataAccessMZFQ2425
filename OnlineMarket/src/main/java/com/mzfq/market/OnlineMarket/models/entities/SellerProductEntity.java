package com.mzfq.market.OnlineMarket.models.entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "seller_products")
public class SellerProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_product_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private SellersEntity seller;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @NotNull(message = "Price cannot be null!")
    @Min(value = 0, message = "Price must be greater than or equal to 0!")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private double price;

    @Min(value = 0, message = "Offer price must be greater than or equal to 0!")
    @Column(name = "offer_price", precision = 10, scale = 2)
    private Double offerPrice;

    @Column(name = "offer_start_date")
    private Date offerStartDate;

    @Column(name = "offer_end_date")
    private Date offerEndDate;

    @NotNull(message = "Stock cannot be null!")
    @Min(value = 0, message = "Stock must be greater than or equal to 0!")
    @Column(name = "stock", nullable = false, columnDefinition = "integer default 0")
    private int stock;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public SellersEntity getSeller() { return seller; }
    public void setSeller(SellersEntity seller) { this.seller = seller; }

    public ProductEntity getProduct() { return product; }
    public void setProduct(ProductEntity product) { this.product = product; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Double getOfferPrice() { return offerPrice; }
    public void setOfferPrice(Double offerPrice) { this.offerPrice = offerPrice; }

    public Date getOfferStartDate() { return offerStartDate; }
    public void setOfferStartDate(Date offerStartDate) { this.offerStartDate = offerStartDate; }

    public Date getOfferEndDate() { return offerEndDate; }
    public void setOfferEndDate(Date offerEndDate) { this.offerEndDate = offerEndDate; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}

