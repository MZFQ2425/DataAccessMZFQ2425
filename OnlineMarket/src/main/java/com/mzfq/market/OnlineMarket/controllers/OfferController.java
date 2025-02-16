package com.mzfq.market.OnlineMarket.controllers;

import com.mzfq.market.OnlineMarket.models.dto.OfferDTO;
import com.mzfq.market.OnlineMarket.models.entities.ProductEntity;
import com.mzfq.market.OnlineMarket.models.entities.SellerProductEntity;
import com.mzfq.market.OnlineMarket.services.ProductService;
import com.mzfq.market.OnlineMarket.services.SellerProductService;
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
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private SellersService sellersService;

    @Autowired
    private SellerProductService sellerProductService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String getAvailableProducts(Model model) {
        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Integer sellerId = sellersService.getSellerByCif(cif).getSellerId();

        List<ProductEntity> availableList = sellerProductService.getProductsFromIdSeller(sellerId);

        if (availableList.isEmpty()) {
            model.addAttribute("koMessage", "There's no products in this seller's store");
        }

        model.addAttribute("product", availableList);
        model.addAttribute("offerDTO", new OfferDTO());

        return "offer";
    }

    @PostMapping("/add")
    public String addProductToStore(
        @ModelAttribute("offerDTO") @Valid OfferDTO offerDTO,
        BindingResult result,
        RedirectAttributes redirectAttributes,
        Model model) {

        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Integer sellerId = sellersService.getSellerByCif(cif).getSellerId();

        List<ProductEntity> availableList = sellerProductService.getProductsFromIdSeller(sellerId);
        model.addAttribute("product", availableList);

        if(result.hasErrors()){
            model.addAttribute("offerDTO", offerDTO);
            model.addAttribute("koMessage", "Please correct the incorrect fields");
            return "offer";
        }

        try {

            boolean offerOverlaps = sellerProductService.checkIfOfferAlreadyExists(offerDTO.getProductId(), sellerId, offerDTO.getOfferStartDate(), offerDTO.getOfferEndDate());

            if(offerOverlaps){
                model.addAttribute("offerDTO", offerDTO);
                model.addAttribute("koMessage", "The offer already exists for another product, please correct the date ranges.");
                return "offer";
            }

            SellerProductEntity sellerProduct = sellerProductService.getSellerProductByProductAndSellerID(offerDTO.getProductId(), sellerId);

            //if discount is invalid for the date range
            if (!sellerProductService.isDiscountValid(offerDTO.getOfferStartDate(), offerDTO.getOfferEndDate(), offerDTO.getDiscount())) {
                model.addAttribute("offerDTO", offerDTO);
                model.addAttribute("koMessage", "The offer cannot exceed the maximum for the entered days.");
                return "offer";
            }

            //calculating the price discounted
            Double priceDiscounted = sellerProductService.calculateDiscount(offerDTO.getDiscount(), sellerProduct.getPrice());

            int update = sellerProductService.updateSellerProduct(offerDTO.getProductId(), sellerId, priceDiscounted, offerDTO.getOfferStartDate(), offerDTO.getOfferEndDate());

            //update of the product
            if (update > 0) {
                redirectAttributes.addFlashAttribute("okMessage", "Offer successfully added");
                return "redirect:/offer";
            } else {
                model.addAttribute("offerDTO", offerDTO);
                model.addAttribute("koMessage", "There was a problem updating this offer, please try again later.");
                return "offer";
            }
        }catch(Exception e){
            model.addAttribute("offerDTO", offerDTO);
            model.addAttribute("koMessage", "Error adding offer: " + e.getMessage());
            System.out.println("ERROR: " + e.getMessage());
            return "offer";
        }
    }

    @GetMapping(value = "/json", produces = "application/json")
    @ResponseBody
    public SellerProductEntity getDataJson(@RequestParam Integer productId) {
        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Integer sellerId = sellersService.getSellerByCif(cif).getSellerId();

        SellerProductEntity sellerProduct = sellerProductService.getSellerProductByProductAndSellerID(productId, sellerId);

        System.out.println(sellerProduct.getOfferEndDate());

        return sellerProduct;
    }
}
