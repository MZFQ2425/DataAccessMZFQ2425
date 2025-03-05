package com.mzfq.market.OnlineMarket.models.dao;

import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISellersEntityDAO extends JpaRepository<SellersEntity, Integer> {
    SellersEntity findByCif(String cif);

    @Query(value = "SELECT * FROM sellers s INNER JOIN seller_products sp ON sp.seller_id = s.seller_id AND sp.product_id = :productId", nativeQuery = true)
    List<SellersEntity> getInfoSellersFromProductId(@Param("productId") Integer productId);

}