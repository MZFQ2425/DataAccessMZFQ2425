package com.mzfq.market.OnlineMarket.controllers;

import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.services.SellersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ViewController {
    private SellersService sellersService;

    public ViewController(SellersService sellersService) {
        this.sellersService = sellersService;
    }

    @GetMapping("/")
    public String index(Model model) {
        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        SellersEntity seller = sellersService.getSellerByCif(cif);

        model.addAttribute("seller", seller);

        return "index";
    }
}
