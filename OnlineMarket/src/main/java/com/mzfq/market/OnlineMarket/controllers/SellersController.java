package com.mzfq.market.OnlineMarket.controllers;

import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.services.SellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/sellers")
public class SellersController {

    @Autowired
    private SellersService sellersService;

    // new method to test GET /sellers/{cif}
    @GetMapping("/{cif}")
    public String getSellerByCif(@PathVariable String cif, Model model) {
        SellersEntity seller = sellersService.getSellerByCif(cif);
        if (seller != null) {
            model.addAttribute("seller", seller);
            return "index";
        } else {
            throw new SellerNotFoundException("Seller with CIF " + cif + " not found"); // Lanza una excepción
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class SellerNotFoundException extends RuntimeException {
        public String message;

        public SellerNotFoundException(String message) {
            this.message = message;
        }
    }

    @PostMapping("/update")
    public String updateSeller(@Valid @ModelAttribute("seller") SellersEntity seller, BindingResult result, RedirectAttributes redirectAttributes, Model model) {

        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        seller.setCif(cif);

        // Check for existing sellers
        SellersEntity existingSeller = sellersService.getSellerByCif(cif);
        if (existingSeller == null) {
            model.addAttribute("koMessage", "Seller not found");
            return "index";
        }

        if(result.hasErrors()) {
            model.addAttribute("seller", seller);
            model.addAttribute("koMessage", "Please correct the incorrect fields");
            return "index";
        }

        try {
            if(sellersService.checkIfEqual(sellersService.getSellerByCif(cif), seller)){
                redirectAttributes.addFlashAttribute("koMessage", "There's no changes to update to this seller");
            } else {
                redirectAttributes.addFlashAttribute("okMessage", "Seller successfully updated! :)");
                sellersService.updateSeller(seller);
            }
        } catch (Exception e) {
            model.addAttribute("seller", seller);
            model.addAttribute("koMessage", "Error detected!\n" + e.getMessage());
            return "index";
        }

        return "redirect:/";
    }
}
