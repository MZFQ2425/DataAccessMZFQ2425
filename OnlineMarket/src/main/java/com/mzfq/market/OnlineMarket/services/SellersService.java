package com.mzfq.market.OnlineMarket.services;

import com.mzfq.market.OnlineMarket.models.dao.ISellersEntityDAO;
import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellersService {
    @Autowired
    private ISellersEntityDAO sellersDAO;

    public List<SellersEntity> getAllSellers(){
        return  (List<SellersEntity>) sellersDAO.findAll();
    }

    public Optional<SellersEntity> getSellerById(int id){
        return  sellersDAO.findById(id);
    }

    public void updateSeller(SellersEntity seller){
        SellersEntity sellerExists = sellersDAO.findByCif(seller.getCif());
        HashUtil hash = new HashUtil();

        if(sellerExists==null){
            throw new RuntimeException("Seller not found");
        }else{
            sellerExists.setName(seller.getName());
            sellerExists.setBusinessName(seller.getBusinessName());
            sellerExists.setPhone(seller.getPhone());
            sellerExists.setEmail(seller.getEmail());
            sellerExists.setPlainPassword(seller.getPlainPassword());
            sellerExists.setPassword(hash.hashPassword(seller.getPlainPassword()));

            sellersDAO.save(sellerExists);
        }
    }

    public SellersEntity getSellerByCif(String cif){
        return sellersDAO.findByCif(cif);
    }

    public boolean checkIfEqual(SellersEntity original, SellersEntity sellerChanged){
        if(original.getName().equals(sellerChanged.getName()) &&
            original.getBusinessName().equals(sellerChanged.getBusinessName()) &&
            original.getEmail().equals(sellerChanged.getEmail()) &&
            original.getPhone().equals(sellerChanged.getPhone()) &&
            original.getPlainPassword().equals(sellerChanged.getPlainPassword())){
            return false;
        }else{
            return true;
        }
    }
}
