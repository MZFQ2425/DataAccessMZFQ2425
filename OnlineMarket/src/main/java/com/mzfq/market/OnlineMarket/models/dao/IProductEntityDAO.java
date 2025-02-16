package com.mzfq.market.OnlineMarket.models.dao;

import com.mzfq.market.OnlineMarket.models.entities.ProductEntity;
import com.mzfq.market.OnlineMarket.models.entities.SellerProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductEntityDAO extends JpaRepository<ProductEntity, Integer> {

    @Query(value = "SELECT * FROM get_products_not_in_store_mzfq(:sellerId, NULL)", nativeQuery = true)
    List<ProductEntity> getProductsNotInStore(@Param("sellerId") Integer sellerId);

    @Query(value = "SELECT * FROM get_products_not_in_store_mzfq(:sellerId, :categoryId)", nativeQuery = true)
    List<ProductEntity> getProductsNotInStoreByCategory(@Param("sellerId") Integer sellerId, @Param("categoryId") Integer categoryId);

    @Query(value = "SELECT DISTINCT p.* FROM seller_products sp INNER JOIN products p ON p.product_id = sp.product_id WHERE sp.seller_id = :sellerId", nativeQuery = true)
    List<ProductEntity> getProductsInStoreBySeller(@Param("sellerId") Integer sellerId);
}
