package com.mzfq.market.OnlineMarket.models.dao;

import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISellersEntityDAO extends CrudRepository<SellersEntity, Integer> {
    SellersEntity findByCif(String cif);
}