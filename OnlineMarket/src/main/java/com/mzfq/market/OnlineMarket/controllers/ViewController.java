package com.mzfq.market.OnlineMarket.controllers;

import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.services.SellersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ViewController {

    @Autowired
    private SellersService sellersService;

    @Operation(summary = "Get index page", description = "Retrieves the index page for the seller.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Index page retrieved successfully.")
    })
    @GetMapping("/")
    public String index(Model model) {
        String cif = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        SellersEntity seller = sellersService.getSellerByCif(cif);
        model.addAttribute("seller", seller);
        return "index";
    }
}
