package com.mzfq.market.OnlineMarket.services;

import com.mzfq.market.OnlineMarket.models.dao.ICategoryEntityDAO;
import com.mzfq.market.OnlineMarket.models.dao.IProductEntityDAO;
import com.mzfq.market.OnlineMarket.models.entities.CategoryEntity;
import com.mzfq.market.OnlineMarket.models.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private ICategoryEntityDAO categoryEntityDAO;

    @Autowired
    private IProductEntityDAO productEntityDAO;

    public List<CategoryEntity> getAllCategories() {
        return categoryEntityDAO.findAll();
    }

    public List<CategoryEntity> getAvailableCategories(Integer sellerId) {
        if (sellerId == null) {
            throw new IllegalArgumentException("Seller ID cannot be null.");
        }

        List<ProductEntity> productsNotInStore = productEntityDAO.getProductsNotInStore(sellerId);

        List<Integer> categoryIdsWithProducts = productsNotInStore.stream()
                .map(ProductEntity::getCategoryId)
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toList());

        List<CategoryEntity> allCategories = categoryEntityDAO.findAll();

        List<CategoryEntity> availableCategories = allCategories.stream()
                .filter(category -> categoryIdsWithProducts.contains(category.getCategoryId()))
                .collect(Collectors.toList());

        return availableCategories;
    }


}
