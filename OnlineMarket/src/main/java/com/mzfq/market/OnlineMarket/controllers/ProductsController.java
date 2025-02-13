package com.mzfq.market.OnlineMarket.controllers;

import com.mzfq.market.OnlineMarket.models.dto.ProductDTO;
import com.mzfq.market.OnlineMarket.models.entities.CategoryEntity;
import com.mzfq.market.OnlineMarket.models.entities.ProductEntity;
import com.mzfq.market.OnlineMarket.services.CategoryService;
import com.mzfq.market.OnlineMarket.services.ProductService;
import com.mzfq.market.OnlineMarket.services.SellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SellersService sellersService;

    @GetMapping
    public String getProductsPage(@RequestParam(value = "categoryId", required = false) Integer categoryId, Model model) {
        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Integer sellerId = sellersService.getSellerByCif(cif).getSellerId();

        List<CategoryEntity> availableCategories = categoryService.getAvailableCategories(sellerId);

        if (availableCategories.isEmpty()) {
            model.addAttribute("koMessage", "There's no categories available for your products.");
        }

        model.addAttribute("categories", availableCategories);
        model.addAttribute("product", new ProductEntity());

        return "product";
    }

    @GetMapping(value = "/json", produces = "application/json")
    @ResponseBody
    public List<CategoryEntity> getCategoriesJson() {
        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Integer sellerId = sellersService.getSellerByCif(cif).getSellerId();

        List<CategoryEntity> availableCategories = categoryService.getAvailableCategories(sellerId);
        return availableCategories;
    }

    @GetMapping(value="/jsonProducts", produces = "application/json")
    @ResponseBody
    public List<ProductEntity> getProductsJson(@RequestParam Integer categoryId) {
        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Integer sellerId = sellersService.getSellerByCif(cif).getSellerId();

        List<ProductEntity> availableProducts = productService.getProductsNotInStoreByCategory(sellerId,categoryId);

        return availableProducts;
    }

    @PostMapping("/add")
    public String addProductToStore(
            @ModelAttribute("ProductDTO") @Valid ProductDTO productDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("koMessage", "Please correct the incorrect fields.");
            return "product";
        }

        if (productDTO.getStock() <= 0) {
            model.addAttribute("koMessage", "Stock must be greater than 0.");
            return "product";
        }

        if (productDTO.getPrice() <= 0) {
            model.addAttribute("koMessage", "Price must be greater than 0.");
            return "product";
        }

        try {
            String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            Integer sellerId = sellersService.getSellerByCif(cif).getSellerId();

            productService.addProductToStore(sellerId, productDTO.getProductId(), productDTO.getStock(), productDTO.getPrice());

            redirectAttributes.addFlashAttribute("okMessage", "Product added successfully!");
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("koMessage", "Error adding product: " + e.getMessage());
            return "redirect:/products";
        }
    }

}
