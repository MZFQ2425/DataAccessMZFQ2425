package org.mzfq2425.finalactivity.finalactivity.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sellers")
public class Sellers implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private int sellerId;

    @Column(name = "cif")
    private String cif;

    @Column(name = "name")
    private String name;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "plain_password")
    private String plainPassword;

    @Column(name = "password")
    private String password;

    @Column(name = "url")
    private String url;

    @Column(name = "pro")
    private Boolean pro;

    public Sellers() {

    }

    public Sellers(int sellerId, String cif, String name, String businessName, String phone,
                   String email, String plainPassword, String password, String url, Boolean pro) {
        this.sellerId = sellerId;
        this.cif = cif;
        this.name = name;
        this.businessName = businessName;
        this.phone = phone;
        this.email = email;
        this.plainPassword = plainPassword;
        this.password = password;
        this.url = url;
        this.pro = pro;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getPro() {
        return pro;
    }

    public void setPro(Boolean pro) {
        this.pro = pro;
    }
}
