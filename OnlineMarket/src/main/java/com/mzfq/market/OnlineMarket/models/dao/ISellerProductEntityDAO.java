package com.mzfq.market.OnlineMarket.models.dao;

import com.mzfq.market.OnlineMarket.models.entities.SellerProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Repository
public interface ISellerProductEntityDAO extends JpaRepository<SellerProductEntity, Integer> {

    @Query(value = "SELECT public.is_discount_active_mzfq(:productId, :sellerId, :startDate, :endDate)", nativeQuery = true)
    Boolean checkIfAlreadyExists(@Param("productId") Integer productId, @Param("sellerId") Integer sellerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT * FROM seller_products WHERE product_id = :productId AND seller_id = :sellerId", nativeQuery = true)
    SellerProductEntity getSellerProductByIdAndSeller(@Param("productId") Integer productId, @Param("sellerId") Integer sellerId);

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
