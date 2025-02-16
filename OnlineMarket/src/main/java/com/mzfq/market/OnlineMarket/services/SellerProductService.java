package com.mzfq.market.OnlineMarket.services;

import com.mzfq.market.OnlineMarket.models.dao.IProductEntityDAO;
import com.mzfq.market.OnlineMarket.models.dao.ISellerProductEntityDAO;
import com.mzfq.market.OnlineMarket.models.entities.ProductEntity;
import com.mzfq.market.OnlineMarket.models.entities.SellerProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class SellerProductService  {

    @Autowired
    private ISellerProductEntityDAO sellerProductDAO;

    @Autowired
    private IProductEntityDAO productDAO;

    public List<ProductEntity> getProductsFromIdSeller(int idSeller){
        List<ProductEntity> productsList = productDAO.getProductsInStoreBySeller(idSeller);

        return productsList;
    }

    public boolean checkIfOfferAlreadyExists(int productId, LocalDate startDate, LocalDate endDate){
        return sellerProductDAO.checkIfAlreadyExists(productId, startDate, endDate);
    }
    public boolean isDiscountValid(LocalDate startDate, LocalDate endDate, int discountEntered){

        // Calcular la diferencia en días
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        int maxDiscount = 10;
        if (daysBetween >= 30) {
            maxDiscount = 10;
        } else if (daysBetween >= 15) {
            maxDiscount = 15;
        } else if (daysBetween >= 7) {
            maxDiscount = 20;
        } else if (daysBetween >= 3) {
            maxDiscount = 30;
        } else if (daysBetween >= 1) {
            maxDiscount = 50;
        } else {
            return false;
        }

        boolean isValid = false;
        if (discountEntered <= maxDiscount) {
            isValid = true;
        }

        return isValid;
    }

    public SellerProductEntity getSellerProductByProductAndSellerID(int productId,int sellerId){
        return sellerProductDAO.getSellerProductByIdAndSeller(productId, sellerId);
    }

    public Double calculateDiscount (Integer discount, Double price){
        Double discountedPrice = price - (price * discount / 100);
        return discountedPrice;
    }

    public int updateSellerProduct(int productId, int sellerId, Double offerPrice, LocalDate offerStartDate, LocalDate offerEndDate){
        return sellerProductDAO.updateSellerProduct(productId, sellerId, offerPrice, offerStartDate, offerEndDate);
    }

    //TODO: mostrar ofertas activas para el product seleccionado

}
