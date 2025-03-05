package com.mzfq.market.OnlineMarket.controllers;

import com.mzfq.market.OnlineMarket.models.dto.LoginDTO;
import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.services.SellersService;
import com.mzfq.market.OnlineMarket.utils.HashUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RestEndpointController {

    @Autowired
    private SellersService sellersService;

    @GetMapping("/rest-endpoint")
    public JSONArray showLoginForm(@RequestParam(value = "productId", required = true) String productId) {
        JSONArray jsonList = new JSONArray();

        if(productId!=null){
            List<SellersEntity> sellerList = sellersService.getInfoSellersFromProductId(Integer.valueOf(productId));
            for (SellersEntity seller : sellerList){
                JSONObject jsonSeller = new JSONObject();
                jsonSeller.put("ID", seller.getSellerId());
                jsonSeller.put("CIF",seller.getCif());
                jsonSeller.put("Phone",seller.getPhone());
                jsonSeller.put("Email", seller.getEmail());
                jsonList.add(jsonSeller);
            }
        }else{
            System.out.println("Please, introduce the productId param");
             return null;
        }

        return jsonList;
    }
}
