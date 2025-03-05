package com.mzfq.market.OnlineMarket.controllers;

import com.mzfq.market.OnlineMarket.models.dto.OfferDTO;
import com.mzfq.market.OnlineMarket.models.dto.ProductDTO;
import com.mzfq.market.OnlineMarket.models.dto.ProductListDTO;
import com.mzfq.market.OnlineMarket.models.entities.CategoryEntity;
import com.mzfq.market.OnlineMarket.models.entities.ProductEntity;
import com.mzfq.market.OnlineMarket.models.entities.SellerProductEntity;
import com.mzfq.market.OnlineMarket.services.CategoryService;
import com.mzfq.market.OnlineMarket.services.ProductService;
import com.mzfq.market.OnlineMarket.services.SellerProductService;
import com.mzfq.market.OnlineMarket.services.SellersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/productList")
public class ProductsListController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SellersService sellersService;

    @Autowired
    private SellerProductService sellerProductService;

    //@Operation(summary = "Get products list page", description = "Displays the products list page associated to logged in seller.")
    @GetMapping
    public String getProductsListPage(@RequestParam(value = "isChecked", required = false) String isChecked, Model model) {
        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Integer sellerId = sellersService.getSellerByCif(cif).getSellerId();

        List<ProductEntity> productList = sellerProductService.getProductsFromIdSeller(sellerId);

        List<ProductListDTO> newProductListDTO = new ArrayList<ProductListDTO>();

        for(ProductEntity product : productList){
            int idProduct = product.getProductId();

            SellerProductEntity sellerProduct = null;
            if(isChecked!=null){
                if(isChecked.equals("on")){
                    sellerProduct = sellerProductService.getSellerProductByProductAndSellerIDActiveOffers(idProduct,sellerId);
                }else{
                    sellerProduct = sellerProductService.getSellerProductByProductAndSellerID(idProduct,sellerId);
                }
            }else{
                sellerProduct = sellerProductService.getSellerProductByProductAndSellerID(idProduct,sellerId);
            }

            if(sellerProduct!=null){
                ProductListDTO completeProduct = new ProductListDTO();
                completeProduct.setProductName(product.getProductName());
                completeProduct.setProductId(product.getProductId());
                completeProduct.setCategoryId(product.getCategoryId());
                completeProduct.setPrice(sellerProduct.getPrice());
                if(sellerProduct.getOfferPrice()==null){
                    completeProduct.setOfferPrice(0.0);
                }else {
                    completeProduct.setOfferPrice(sellerProduct.getOfferPrice());
                }
                completeProduct.setCategoryName(product.getCategory().getCategoryName());
                completeProduct.setChecked(false);

                newProductListDTO.add(completeProduct);
            }
        }

        model.addAttribute("productList", newProductListDTO);

        return "productList";
    }
}
