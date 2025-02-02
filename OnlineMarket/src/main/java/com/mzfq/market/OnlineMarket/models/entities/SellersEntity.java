package com.mzfq.market.OnlineMarket.models.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "sellers")
public class SellersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private int sellerId;

    @Column(name = "cif")
    @NotEmpty(message = "CIF cannot be empty!")
    @Size(max = 20, message="CIF size must be 20 or less characters")
    private String cif;

    @Column(name = "name")
    @NotEmpty(message = "The name cannot be empty!")
    @Size(max = 100, message="Name size must be 100 or less characters")
    private String name;

    @Column(name = "business_name")
    @Size(max = 100, message="Name size must be 100 or less characters")
    private String businessName;

    @Column(name = "phone")
    @Size(max = 100, message="Name size must be 100 or less characters")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{3}$", message = "Phone must be in the format 'XXX-XXX-XXX'")
    private String phone;

    @Column(name = "email")
    @Size(max = 90, message="Email size must be 90 or less characters")
    @Email(message="Please, enter a valid mail")
    private String email;

    @Column(name = "plain_password")
    @NotEmpty(message = "The password cannot be empty!")
    @Size(min = 6, max = 50, message="Password size must be between 6 and 50 characters")
    private String plainPassword;

    //we wont use validations on password hashed since it can only be edited on thee server side
    @Column(name = "password")
    private String password;

    public SellersEntity() {

    }

    public SellersEntity(int sellerId, String cif, String name, String businessName, String phone, String email, String plainPassword, String password) {
        this.sellerId = sellerId;
        this.cif = cif;
        this.name = name;
        this.businessName = businessName;
        this.phone = phone;
        this.email = email;
        this.plainPassword = plainPassword;
        this.password = password;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
