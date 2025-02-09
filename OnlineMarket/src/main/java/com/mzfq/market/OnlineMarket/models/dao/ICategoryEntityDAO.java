package com.mzfq.market.OnlineMarket.models.dao;

import com.mzfq.market.OnlineMarket.models.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryEntityDAO extends JpaRepository<CategoryEntity, Integer> {

    @Query(value = "SELECT DISTINCT c.* " +
            "FROM categories c " +
            "JOIN products p ON c.category_id = p.category_id " +
            "WHERE p.product_id NOT IN (SELECT sp.product_id FROM seller_products sp WHERE sp.seller_id = :sellerId)",
            nativeQuery = true)
    List<CategoryEntity> findAvailableCategories(@Param("sellerId") Integer sellerId);
}