package com.mzfq.market.OnlineMarket.models.dao;

import com.mzfq.market.OnlineMarket.models.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductEntityDAO extends JpaRepository<ProductEntity, Integer> {

    @Query(value = "SELECT * FROM get_products_not_in_store(:sellerId, NULL)", nativeQuery = true)
    List<ProductEntity> getProductsNotInStore(@Param("sellerId") Integer sellerId);

    @Query(value = "SELECT * FROM get_products_not_in_store(:sellerId, :categoryId)", nativeQuery = true)
    List<ProductEntity> getProductsNotInStoreByCategory(@Param("sellerId") Integer sellerId, @Param("categoryId") Integer categoryId);
}
