package com.mzfq.market.OnlineMarket.models.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "offer_price")
    private Double offerPrice;

    @Column(name = "offer_start_date")
    private LocalDate offerStartDate;

    @Column(name = "offer_end_date")
    private LocalDate offerEndDate;

    @Column(name = "stock", nullable = false)
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

    public LocalDate getOfferStartDate() { return offerStartDate; }
    public void setOfferStartDate(LocalDate offerStartDate) { this.offerStartDate = offerStartDate; }

    public LocalDate getOfferEndDate() { return offerEndDate; }
    public void setOfferEndDate(LocalDate offerEndDate) { this.offerEndDate = offerEndDate; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}

