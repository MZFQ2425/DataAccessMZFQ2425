package com.mzfq.market.OnlineMarket.controllers;

import com.mzfq.market.OnlineMarket.models.dto.LoginDTO;
import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.services.SellersService;
import com.mzfq.market.OnlineMarket.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private SellersService sellersService;

    // method to show the login page
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "expired", required = false) String expired,
                                Model model) {

        if ("true".equals(error)) {
            model.addAttribute("errorMessage", "Invalid credentials, please, try again.");
        }

        if ("true".equals(expired)) {
            model.addAttribute("expiredMessage", "Your session has expired. Please, login again.");
        }

        return "login";  // back to login.html
    }

    // method to manage the user login
    @PostMapping("/login")
    public String login(@Valid LoginDTO loginDTO, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Please, enter valid credentials.");
            return "login";
        }

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        SellersEntity seller = sellersService.getSellerByCif(username);

        if (seller == null) {
            model.addAttribute("errorMessage", "CIF not found.");
            return "login";
        }

        if (!HashUtil.hashPassword(password).equals(seller.getPassword())) {
            model.addAttribute("errorMessage", "Incorrect credentials.");
            return "login";
        }

        //redirect to index
        return "redirect:/";
    }
}
