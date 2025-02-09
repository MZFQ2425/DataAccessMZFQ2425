package com.mzfq.market.OnlineMarket.services;

import com.mzfq.market.OnlineMarket.models.dao.IProductEntityDAO;
import com.mzfq.market.OnlineMarket.models.dao.ISellerProductEntityDAO;
import com.mzfq.market.OnlineMarket.models.entities.CategoryEntity;
import com.mzfq.market.OnlineMarket.models.entities.ProductEntity;
import com.mzfq.market.OnlineMarket.models.entities.SellerProductEntity;
import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private IProductEntityDAO productDAO;

    @Autowired
    private ISellerProductEntityDAO sellerProductRepository;

    @Autowired
    private SellersService sellersService;

    @Autowired
    private CategoryService categoryService;

    public List<ProductEntity> getProductsNotInStoreByCategory(Integer sellerId, Integer categoryId) {
        List<ProductEntity> products = productDAO.getProductsNotInStoreByCategory(sellerId, categoryId);
        return products;
    }

    public List<ProductEntity> getProductsNotInStore(Integer sellerId) {
        List<ProductEntity> products = productDAO.getProductsNotInStore(sellerId);
        return filterProductsWithoutStock(sellerId, products);
    }

    private List<ProductEntity> filterProductsWithoutStock(Integer sellerId, List<ProductEntity> products) {
        return products.stream()
                .filter(product -> {
                    Optional<SellerProductEntity> sellerProduct = sellerProductRepository.findById(product.getProductId());
                    return sellerProduct.isEmpty();
                })
                .collect(Collectors.toList());
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public void addProductToStore(Integer sellerId, Integer productId, int stock, double price) {

        SellersEntity seller = sellersService.getSellerById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        ProductEntity product = productDAO.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        SellerProductEntity sellerProduct = new SellerProductEntity();
        sellerProduct.setSeller(seller);
        sellerProduct.setProduct(product);
        sellerProduct.setStock(stock);
        sellerProduct.setPrice(price);

        sellerProductRepository.save(sellerProduct);
    }
}
