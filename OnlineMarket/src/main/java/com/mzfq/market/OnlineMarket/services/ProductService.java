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
        if (sellerId == null || categoryId == null) {
            throw new IllegalArgumentException("Seller ID and Category ID cannot be null.");
        }
        List<ProductEntity> products = productDAO.getProductsNotInStoreByCategory(sellerId, categoryId);
        return products;
    }

    public void addProductToStore(Integer sellerId, Integer productId, int stock, double price) {
        if (sellerId == null || productId == null) {
            throw new IllegalArgumentException("Seller ID and Product ID cannot be null.");
        }

        if (stock <= 0) {
            throw new IllegalArgumentException("Stock must be greater than 0.");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }

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
