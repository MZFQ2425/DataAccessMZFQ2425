package com.mzfq.market.OnlineMarket.services;

import com.mzfq.market.OnlineMarket.models.dao.ISellersEntityDAO;
import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.utils.HashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellersServiceTest {

    @Mock
    private ISellersEntityDAO sellersDAO;

    @InjectMocks
    private SellersService sellersService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getSellerByCifOk() {
        // setup existing seller
        String cif = "B23456789";
        SellersEntity seller = new SellersEntity(2, cif, "FashionHub", "FashionHub Corp.", "987-654-321", "fashionhub@example.com", "password2", "hashedPassword");

        //expect to recover seller from cif
        when(sellersDAO.findByCif(cif)).thenReturn(seller);

        //recover seller from cif
        SellersEntity result = sellersService.getSellerByCif(cif);

        // validate that it's not null and the cif is correct
        assertNotNull(result);
        assertEquals(cif, result.getCif());
    }

    @Test
    public void getSellerByCifNotFound() {
        // setup non-existing seller
        String cif = "NonExistentCIF";

        // expect to recover null
        when(sellersDAO.findByCif(cif)).thenReturn(null);

        // recover seller from the service
        SellersEntity result = sellersService.getSellerByCif(cif);

        // validate that the result is null
        assertNull(result);
    }

    @Test
    public void updateSellerOk() {
        // setup existing seller
        String cif = "B23456789";
        SellersEntity existingSeller = new SellersEntity();
        existingSeller.setCif(cif);
        existingSeller.setName("FashionHub");
        existingSeller.setPlainPassword("oldPassword");
        existingSeller.setPassword(HashUtil.hashPassword("oldPassword"));

        // setup updated seller
        SellersEntity updatedSeller = new SellersEntity();
        updatedSeller.setCif(cif);
        updatedSeller.setName("FashionHub Updated");
        updatedSeller.setPlainPassword("newPassword");

        // expect that the existingSeller exists
        when(sellersDAO.findByCif(cif)).thenReturn(existingSeller);

        //call to update
        sellersService.updateSeller(updatedSeller);

        // validate that the data was updated correctly
        assertEquals("FashionHub Updated", existingSeller.getName());
        assertEquals("newPassword", existingSeller.getPlainPassword());
        assertEquals(HashUtil.hashPassword("newPassword"), existingSeller.getPassword());
    }

    @Test
    public void updateSellerInvalidData() {
        // create invalid seller
        String cif = "B23456789";
        SellersEntity invalidSeller = new SellersEntity();
        invalidSeller.setCif(cif);
        invalidSeller.setName("");
        invalidSeller.setPlainPassword("");

        // expect to return a new empty sellersEntity since it simulates finding an existing seller with the provided cif
        when(sellersDAO.findByCif(cif)).thenReturn(new SellersEntity());

        // verify that an exception is thrown
        assertThrows(RuntimeException.class, () -> sellersService.updateSeller(invalidSeller));
    }

    @Test
    public void updateSellerNotFound() {
        // set a seller that does not exist
        String cif = "NonExistentCIF";
        SellersEntity nonExistentSeller = new SellersEntity();
        nonExistentSeller.setCif(cif);
        nonExistentSeller.setName("NonExistent");
        nonExistentSeller.setPlainPassword("password");

        //expect to recover null
        when(sellersDAO.findByCif(cif)).thenReturn(null);

        // verify that an exception is thrown
        assertThrows(RuntimeException.class, () -> sellersService.updateSeller(nonExistentSeller));
    }
}