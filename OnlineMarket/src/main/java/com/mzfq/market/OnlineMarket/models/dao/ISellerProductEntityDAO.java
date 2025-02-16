package com.mzfq.market.OnlineMarket.models.dao;

import com.mzfq.market.OnlineMarket.models.dto.FullProductDTO;
import com.mzfq.market.OnlineMarket.models.entities.SellerProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;

@Repository
public interface ISellerProductEntityDAO extends JpaRepository<SellerProductEntity, Integer> {

    @Query(value = "SELECT public.is_discount_active_mzfq(:productId, :startDate, :endDate)", nativeQuery = true)
    Boolean checkIfAlreadyExists(@Param("productId") Integer productId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT * FROM seller_products WHERE product_id = :productId AND seller_id = :sellerId", nativeQuery = true)
    SellerProductEntity getSellerProductByIdAndSeller(@Param("productId") Integer productId, @Param("sellerId") Integer sellerId);

    @Query(value = "SELECT p.product_id, p.product_name, sp.offer_price, sp.offer_start_date, sp.offer_end_date FROM seller_products sp " +
            "INNER JOIN products p ON p.product_id = sp.product_id " +
            "WHERE sp.product_id = :productId AND sp.seller_id = :sellerId", nativeQuery = true)
    FullProductDTO getFullSellerProductByIdAndSeller(@Param("productId") Integer productId, @Param("sellerId") Integer sellerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE seller_products " +
            "SET offer_price = :offerPrice, " +
            "offer_start_date = :offerStartDate, " +
            "offer_end_date = :offerEndDate " +
            "WHERE product_id = :productId AND seller_id = :sellerId",
            nativeQuery = true)
    int updateSellerProduct(@Param("productId") Integer productId,
                           @Param("sellerId") Integer sellerId,
                           @Param("offerPrice") Double offerPrice,
                           @Param("offerStartDate") LocalDate offerStartDate,
                           @Param("offerEndDate") LocalDate offerEndDate);
}
